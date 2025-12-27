package com.easyjob.easyjobapi.core.offerApplicationEvaluation.services;

import com.easyjob.easyjobapi.core.firm.management.FirmOwnerMismatchException;
import com.easyjob.easyjobapi.core.offerApplication.management.OfferApplicationManager;
import com.easyjob.easyjobapi.core.offerApplication.management.OfferApplicationMapper;
import com.easyjob.easyjobapi.core.offerApplication.management.OfferApplicationNotFoundException;
import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplicationDAO;
import com.easyjob.easyjobapi.core.offerApplicationEvaluation.management.OfferApplicationEvaluationManager;
import com.easyjob.easyjobapi.core.offerApplicationEvaluation.management.OfferApplicationEvaluationMapper;
import com.easyjob.easyjobapi.core.offerApplicationEvaluation.models.OfferApplicationEvaluationAIResponse;
import com.easyjob.easyjobapi.core.offerApplicationEvaluation.models.OfferApplicationEvaluationDAO;
import com.easyjob.easyjobapi.core.user.management.UserNotRecruiterException;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileMapper;
import com.easyjob.easyjobapi.modules.applierProfile.services.ApplierProfileBuilders;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.management.EducationManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models.EducationDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.management.ProjectManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.ProjectDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.management.SkillManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.models.SkillDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.management.WorkExperienceManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.models.WorkExperienceDAO;
import com.easyjob.easyjobapi.utils.EvaluationPromptBuilder;
import com.easyjob.easyjobapi.utils.claude.ClaudeEvaluateCandidateService;
import com.easyjob.easyjobapi.utils.enums.ProcessStatusEnum;
import com.easyjob.easyjobapi.utils.enums.RecommendationEnum;
import com.easyjob.easyjobapi.utils.enums.UserTypeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OfferApplicationEvaluationEvaluateService {
    private final HttpServletRequest httpServletRequest;
    private final OfferApplicationManager offerApplicationManager;
    private final OfferApplicationMapper offerApplicationMapper;
    private final ApplierProfileMapper applierProfileMapper;
    private final OfferApplicationEvaluationManager offerApplicationEvaluationManager;
    private final OfferApplicationEvaluationMapper offerApplicationEvaluationMapper;
    private final WorkExperienceManager workExperienceManager;
    private final ProjectManager projectManager;
    private final SkillManager skillManager;
    private final EducationManager educationManager;
    private final ClaudeEvaluateCandidateService claudeEvaluateCandidateService;
    private final ObjectMapper objectMapper;

    @Transactional
    public void evaluate(UUID id) {
        log.info("Evaluating offer application with id: {}", id);
        UserDAO userDAO = (UserDAO) httpServletRequest.getAttribute("user");

        if (!userDAO.getUserType().equals(UserTypeEnum.RECRUITER)) {
            throw new UserNotRecruiterException();
        }

        OfferApplicationDAO offerApplicationDAO = offerApplicationManager.findById(id)
                .orElseThrow(OfferApplicationNotFoundException::new);

        if (!offerApplicationDAO.getOffer().getFirm().getUser().equals(userDAO)) {
            throw new FirmOwnerMismatchException();
        }

        OfferApplicationEvaluationDAO existingEvaluation = offerApplicationEvaluationManager.findByOfferAndApplierProfile(
                offerApplicationDAO.getOffer().getId(),
                offerApplicationDAO.getApplierProfile().getId()
        ).orElse(null);

        if (existingEvaluation != null) {
            if (existingEvaluation.getProcessStatus().equals(ProcessStatusEnum.PROCESSING)) {
                log.info("Evaluation already in progress for application: {}", id);
                return;
            }
            if (existingEvaluation.getProcessStatus().equals(ProcessStatusEnum.COMPLETED)) {
                log.info("Re-evaluating completed evaluation: {}", existingEvaluation.getId());
                existingEvaluation.setProcessStatus(ProcessStatusEnum.PENDING);
                offerApplicationEvaluationManager.saveToDatabase(existingEvaluation);
            }
        }

        OfferApplicationEvaluationDAO evaluationDAO;
        if (existingEvaluation != null) {
            evaluationDAO = existingEvaluation;
        } else {
            evaluationDAO = OfferApplicationEvaluationBuilders.buildEvaluationDAO(
                    offerApplicationDAO,
                    offerApplicationDAO.getApplierProfile()
            );
            evaluationDAO.setProcessStatus(ProcessStatusEnum.PENDING);
        }

        evaluationDAO = offerApplicationEvaluationManager.saveToDatabase(evaluationDAO);
        UUID evaluationId = evaluationDAO.getId();

        log.info("Created evaluation with id: {} and status: PENDING", evaluationId);

        processEvaluationAsync(evaluationId, id);
    }

    private void processEvaluationAsync(UUID evaluationId, UUID offerApplicationId) {
        log.info("Starting async evaluation process for evaluation id: {}", evaluationId);

        OfferApplicationDAO offerApplicationDAO = offerApplicationManager.findById(offerApplicationId)
                .orElseThrow(() -> new RuntimeException("Offer application not found: " + offerApplicationId));

        List<WorkExperienceDAO> workExperiences = ApplierProfileBuilders.buildWorkExperiences(offerApplicationDAO.getApplierProfile(), workExperienceManager);
        List<EducationDAO> educations = ApplierProfileBuilders.buildEducations(offerApplicationDAO.getApplierProfile(), educationManager);
        List<ProjectDAO> projects = ApplierProfileBuilders.buildProjects(offerApplicationDAO.getApplierProfile(), projectManager);
        List<SkillDAO> skills = ApplierProfileBuilders.buildSkills(offerApplicationDAO.getApplierProfile(), skillManager);

        String prompt = EvaluationPromptBuilder.createPrompt(
                offerApplicationDAO.getOffer(),
                skills,
                workExperiences,
                educations,
                projects
        );

        updateEvaluationStatus(evaluationId, ProcessStatusEnum.PROCESSING);

        claudeEvaluateCandidateService.evaluateCandidate(prompt)
                .thenAccept(aiResponse -> {
                    log.info("AI evaluation completed for evaluation id: {}", evaluationId);
                    handleEvaluationSuccess(evaluationId, aiResponse);
                })
                .exceptionally(throwable -> {
                    log.error("AI evaluation failed for evaluation id: {}", evaluationId, throwable);
                    handleEvaluationFailure(evaluationId, throwable);
                    return null;
                });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void updateEvaluationStatus(UUID evaluationId, ProcessStatusEnum status) {
        try {
            OfferApplicationEvaluationDAO evaluation = offerApplicationEvaluationManager
                    .findById(evaluationId)
                    .orElseThrow(() -> new RuntimeException("Evaluation not found: " + evaluationId));

            evaluation.setProcessStatus(status);
            offerApplicationEvaluationManager.saveToDatabase(evaluation);

            log.info("Updated evaluation {} status to {}", evaluationId, status);
        } catch (Exception e) {
            log.error("Failed to update evaluation status", e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void handleEvaluationSuccess(UUID evaluationId, OfferApplicationEvaluationAIResponse aiResponse) {
        try {
            OfferApplicationEvaluationDAO evaluation = offerApplicationEvaluationManager
                    .findById(evaluationId)
                    .orElseThrow(() -> new RuntimeException("Evaluation not found: " + evaluationId));

            evaluation.setOverAllMatchScore(aiResponse.overallScore());
            evaluation.setSkillsScore(aiResponse.skillsScore());
            evaluation.setExperienceScore(aiResponse.experienceScore());
            evaluation.setEducationScore(aiResponse.educationScore());
            evaluation.setProjectScore(aiResponse.projectsScore());

            if (aiResponse.skillsAnalysis() != null) {
                String skillsAnalysisJson = objectMapper.writeValueAsString(aiResponse.skillsAnalysis());
                evaluation.setSkillsAnalysis(skillsAnalysisJson);
            }

            if (aiResponse.strengths() != null && !aiResponse.strengths().isEmpty()) {
                evaluation.setStrengths(String.join("\n• ", aiResponse.strengths()));
            }

            if (aiResponse.weaknesses() != null && !aiResponse.weaknesses().isEmpty()) {
                evaluation.setWeaknesses(String.join("\n• ", aiResponse.weaknesses()));
            }

            if (aiResponse.interviewFocusAreas() != null && !aiResponse.interviewFocusAreas().isEmpty()) {
                evaluation.setInterviewFocusAreas(String.join("\n• ", aiResponse.interviewFocusAreas()));
            }

            evaluation.setCulturalFit(aiResponse.culturalFit());
            evaluation.setGrowthPotential(aiResponse.growthPotential());
            evaluation.setDetailedSummary(aiResponse.detailedSummary());

            if (aiResponse.recommendation() != null) {
                evaluation.setRecommendation(RecommendationEnum.valueOf(aiResponse.recommendation()));
            }

            evaluation.setProcessStatus(ProcessStatusEnum.COMPLETED);

            offerApplicationEvaluationManager.saveToDatabase(evaluation);

            log.info("Successfully saved evaluation results for id: {}", evaluationId);
        } catch (Exception e) {
            log.error("Failed to save evaluation results for id: {}", evaluationId, e);
            handleEvaluationFailure(evaluationId, e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void handleEvaluationFailure(UUID evaluationId, Throwable throwable) {
        try {
            OfferApplicationEvaluationDAO evaluation = offerApplicationEvaluationManager
                    .findById(evaluationId)
                    .orElseThrow(() -> new RuntimeException("Evaluation not found: " + evaluationId));

            evaluation.setProcessStatus(ProcessStatusEnum.FAILED);

            offerApplicationEvaluationManager.saveToDatabase(evaluation);

            log.error("Marked evaluation {} as FAILED: {}", evaluationId, throwable.getMessage());
        } catch (Exception e) {
            log.error("Failed to update evaluation status to FAILED for id: {}", evaluationId, e);
        }
    }
}