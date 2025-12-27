package com.easyjob.easyjobapi.core.offerApplicationEvaluation.services;

import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplication;
import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplicationDAO;
import com.easyjob.easyjobapi.core.offerApplicationEvaluation.models.*;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import com.easyjob.easyjobapi.utils.enums.ProcessStatusEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OfferApplicationEvaluationBuilders {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static OfferApplicationEvaluation build(OfferApplication offerApplication, ApplierProfile applierProfile) {
        return OfferApplicationEvaluation.builder()
                .offerApplicationEvaluationId(OfferApplicationEvaluationId.of(null))
                .offerApplication(offerApplication)
                .applierProfile(applierProfile)
                .processStatus(ProcessStatusEnum.PENDING)
                .build();
    }

    public static OfferApplicationEvaluationDAO buildEvaluationDAO(
            OfferApplicationDAO offerDAO,
            ApplierProfileDAO applierProfileDAO) {

        OfferApplicationEvaluationDAO evaluationDAO = new OfferApplicationEvaluationDAO();
        evaluationDAO.setOfferApplication(offerDAO);
        evaluationDAO.setApplierProfile(applierProfileDAO);
        evaluationDAO.setProcessStatus(ProcessStatusEnum.PENDING);

        return evaluationDAO;
    }

    public static OfferApplicationEvaluationResponse buildResponse(OfferApplicationEvaluationDAO dao) {
        SkillsAnalysisResponse skillsAnalysis = null;

        if (dao.getSkillsAnalysis() != null && !dao.getSkillsAnalysis().isEmpty()) {
            try {
                skillsAnalysis = objectMapper.readValue(
                        dao.getSkillsAnalysis(),
                        SkillsAnalysisResponse.class
                );
            } catch (Exception e) {
                log.error("Failed to parse skillsAnalysis JSON for evaluation {}", dao.getId(), e);
            }
        }

        return OfferApplicationEvaluationResponse.builder()
                .evaluationId(dao.getId())
                .overallMatchScore(dao.getOverAllMatchScore())
                .skillsScore(dao.getSkillsScore())
                .experienceScore(dao.getExperienceScore())
                .educationScore(dao.getEducationScore())
                .projectsScore(dao.getProjectScore())
                .skillsAnalysis(skillsAnalysis)
                .strengths(dao.getStrengths())
                .weaknesses(dao.getWeaknesses())
                .culturalFit(dao.getCulturalFit())
                .growthPotential(dao.getGrowthPotential())
                .interviewFocusArea(dao.getInterviewFocusAreas())
                .detailedSummary(dao.getDetailedSummary())
                .recommendation(dao.getRecommendation())
                .processStatus(dao.getProcessStatus())
                .createdAt(dao.getCreatedAt())
                .build();
    }
}