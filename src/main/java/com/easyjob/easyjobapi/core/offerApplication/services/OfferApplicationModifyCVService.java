package com.easyjob.easyjobapi.core.offerApplication.services;

import com.easyjob.easyjobapi.core.offer.magenement.OfferManager;
import com.easyjob.easyjobapi.core.offer.magenement.OfferNotFoundException;
import com.easyjob.easyjobapi.core.offer.models.OfferDAO;
import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplicationModifyCVRequest;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.files.storage.services.StorageService;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileManager;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileMapper;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.models.*;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.management.CVManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.management.CVMapper;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.management.CVNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CV;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CVDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.services.CVBuilders;
import com.easyjob.easyjobapi.utils.CVModificationPromptBuilder;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import com.easyjob.easyjobapi.utils.claude.ClaudeCVModificationService;
import com.easyjob.easyjobapi.utils.enums.CVTemplateEnum;
import com.easyjob.easyjobapi.utils.enums.ProcessStatusEnum;
import com.easyjob.easyjobapi.utils.pdf.ResumePDFService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OfferApplicationModifyCVService {
    private final HttpServletRequest request;
    private final CVManager cvManager;
    private final CVMapper cvMapper;
    private final OfferManager offerManager;
    private final StorageService storageService;
    private final ClaudeCVModificationService claudeCVModificationService;
    private final ResumePDFService resumePDFService;
    private final ApplierProfileManager applierProfileManager;
    private final ApplierProfileMapper applierProfileMapper;

    @Transactional
    public void modifyCV(OfferApplicationModifyCVRequest modifyRequest, UUID offerId) {
        try {
            log.info("Modifying CV with id: {} for Offer ID: {}", modifyRequest.cvId(), offerId);

            UserDAO userDAO = (UserDAO) request.getAttribute("user");

            CVDAO originalCvDao = cvManager.findById(modifyRequest.cvId())
                    .orElseThrow(CVNotFoundException::new);

            OfferDAO offerDao = offerManager.findById(offerId)
                    .orElseThrow(OfferNotFoundException::new);

            ApplierProfileDAO applierProfileDAO = applierProfileManager.findByUser(userDAO.getId())
                    .orElseThrow(ApplierProfileNotFoundException::new);
            ApplierProfile applierProfile = applierProfileMapper.mapToDomain(applierProfileDAO, new CycleAvoidingMappingContext());

            CV cv = CVBuilders.build(applierProfile, modifyRequest.name(), null, null);
            CVDAO newCvDAO = cvMapper.mapToEntity(cv, new CycleAvoidingMappingContext());
            newCvDAO.setApplierProfile(applierProfileDAO);
            newCvDAO.setProcessStatus(ProcessStatusEnum.PENDING);
            newCvDAO = cvManager.saveToDatabase(newCvDAO);
            UUID newCvId = newCvDAO.getId();

            log.info("Created new modified CV with id: {} and status: PENDING", newCvId);

            processModifiedCVGenerationAsync(
                    newCvId,
                    originalCvDao.getStorageKey(),
                    offerDao,
                    userDAO,
                    applierProfileDAO,
                    modifyRequest
            );

        } catch (CVNotFoundException | OfferNotFoundException | ApplierProfileNotFoundException e) {
            log.error("Resource not found during CV modification", e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to initiate CV modification", e);
            throw new RuntimeException("CV modification failed: " + e.getMessage(), e);
        }
    }

    private void processModifiedCVGenerationAsync(
            UUID newCvId,
            String originalCvStorageKey,
            OfferDAO offerDao,
            UserDAO userDAO,
            ApplierProfileDAO applierProfileDAO,
            OfferApplicationModifyCVRequest modifyRequest
    ) {
        log.info("Starting async CV modification process for new CV id: {}", newCvId);

        byte[] cvBytes;
        try (var cvStream = storageService.getFile(originalCvStorageKey)) {
            cvBytes = cvStream.readAllBytes();
            log.info("Original CV file loaded successfully, size: {} bytes", cvBytes.length);
        } catch (IOException e) {
            log.error("Failed to read original CV file", e);
            handleCVModificationFailure(newCvId, e);
            return;
        }

        String prompt = CVModificationPromptBuilder.createPrompt(offerDao);
        log.info("Prompt built successfully for offer: {}", offerDao.getName());

        updateCVStatus(newCvId, ProcessStatusEnum.PROCESSING);

        claudeCVModificationService.modifyCV(prompt, cvBytes, originalCvStorageKey)
                .thenAccept(modifiedCV -> {
                    if (modifiedCV != null) {
                        log.info("AI CV modification completed for new CV id: {}", newCvId);
                        handleCVModificationSuccess(
                                newCvId,
                                modifiedCV,
                                modifyRequest.cvTemplate(),
                                userDAO,
                                applierProfileDAO
                        );
                    } else {
                        log.warn("Claude AI returned null CV data for new CV id: {}", newCvId);
                        handleCVModificationFailure(newCvId, new RuntimeException("Claude AI returned null CV data"));
                    }
                })
                .exceptionally(throwable -> {
                    log.error("AI CV modification failed for new CV id: {}", newCvId, throwable);
                    handleCVModificationFailure(newCvId, throwable);
                    return null;
                });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void updateCVStatus(UUID cvId, ProcessStatusEnum status) {
        try {
            CVDAO cvDAO = cvManager.findById(cvId)
                    .orElseThrow(() -> new RuntimeException("CV not found: " + cvId));

            cvDAO.setProcessStatus(status);
            cvManager.saveToDatabase(cvDAO);

            log.info("Updated CV {} status to {}", cvId, status);
        } catch (Exception e) {
            log.error("Failed to update CV status", e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void handleCVModificationSuccess(
            UUID cvId,
            ApplierProfileCVModificationResponse modifiedCvData,
            CVTemplateEnum template,
            UserDAO userDAO,
            ApplierProfileDAO applierProfileDAO
    ) {
        try {
            CVDAO cvDAO = cvManager.findById(cvId)
                    .orElseThrow(() -> new RuntimeException("CV not found: " + cvId));

            log.info("Converting modified CV data to ApplierProfileCVResponse for PDF generation");

            ApplierProfileCVResponse cvResponseForPdf = convertToCVResponse(modifiedCvData);

            log.info("Generating PDF with template: {} for modified CV id: {}", template, cvId);

            byte[] cvFile = resumePDFService.generatePDF(cvResponseForPdf, template);

            String storageKey = String.format("%s/cv/%s_modified_%s.pdf",
                    userDAO.getId(),
                    template.name().toLowerCase(),
                    UUID.randomUUID());

            List<String> cvList = applierProfileDAO.getCv() != null
                    ? new ArrayList<>(applierProfileDAO.getCv())
                    : new ArrayList<>();
            cvList.add(storageKey);
            applierProfileDAO.setCv(cvList);
            applierProfileManager.saveToDatabase(applierProfileDAO);
            log.info("Modified CV reference saved to database: {}", storageKey);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(cvFile);
            storageService.uploadFile(storageKey, "application/pdf", outputStream);

            cvDAO.setStorageKey(storageKey);
            cvDAO.setProcessStatus(ProcessStatusEnum.COMPLETED);
            cvManager.saveToDatabase(cvDAO);

            log.info("Successfully generated modified CV with id: {}, template: {}, size: {} bytes",
                    cvId, template, cvFile.length);

        } catch (Exception e) {
            log.error("Failed to save modified CV results for id: {}", cvId, e);
            handleCVModificationFailure(cvId, e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void handleCVModificationFailure(UUID cvId, Throwable throwable) {
        try {
            CVDAO cvDAO = cvManager.findById(cvId)
                    .orElseThrow(() -> new RuntimeException("CV not found: " + cvId));

            cvDAO.setProcessStatus(ProcessStatusEnum.FAILED);
            cvManager.saveToDatabase(cvDAO);

            log.error("Marked CV {} as FAILED: {}", cvId, throwable.getMessage());
        } catch (Exception e) {
            log.error("Failed to update CV status to FAILED for id: {}", cvId, e);
        }
    }

    private ApplierProfileCVResponse convertToCVResponse(ApplierProfileCVModificationResponse modifiedData) {
        ApplierProfileCVPersonalInformationResponse personalInfo = null;
        if (modifiedData.getPersonalInformation() != null) {
            personalInfo = ApplierProfileCVPersonalInformationResponse.builder()
                    .fullName(modifiedData.getPersonalInformation().getFullName())
                    .email(modifiedData.getPersonalInformation().getEmail())
                    .build();
        }

        List<ApplierProfileCVEducationResponse> education = null;
        if (modifiedData.getEducation() != null) {
            education = modifiedData.getEducation().stream()
                    .map(edu -> ApplierProfileCVEducationResponse.builder()
                            .degree(edu.getDegree())
                            .university(edu.getUniversity())
                            .major(edu.getMajor())
                            .startDate(edu.getStartDate())
                            .endDate(edu.getEndDate())
                            .gpa(edu.getGpa())
                            .build())
                    .toList();
        }

        List<ApplierProfileCVWorkExperienceResponse> workExperience = null;
        if (modifiedData.getWorkExperience() != null) {
            workExperience = modifiedData.getWorkExperience().stream()
                    .map(we -> ApplierProfileCVWorkExperienceResponse.builder()
                            .title(we.getTitle())
                            .companyName(we.getCompanyName())
                            .startDate(we.getStartDate())
                            .endDate(we.getEndDate())
                            .location(we.getLocation())
                            .responsibilities(we.getResponsibilities())
                            .build())
                    .toList();
        }

        List<ApplierProfileCVProjectResponse> projects = null;
        if (modifiedData.getProjects() != null) {
            projects = modifiedData.getProjects().stream()
                    .map(proj -> ApplierProfileCVProjectResponse.builder()
                            .name(proj.getName())
                            .description(proj.getDescription())
                            .technologies(proj.getTechnologies())
                            .link(proj.getLink())
                            .build())
                    .toList();
        }

        List<ApplierProfileCVSkillResponse> skills = null;
        if (modifiedData.getSkills() != null) {
            skills = modifiedData.getSkills().stream()
                    .map(skill -> ApplierProfileCVSkillResponse.builder()
                            .name(skill.getName())
                            .level(skill.getLevel())
                            .build())
                    .toList();
        }

        return ApplierProfileCVResponse.builder()
                .personalInformation(personalInfo)
                .education(education)
                .workExperience(workExperience)
                .projects(projects)
                .skills(skills)
                .summary(modifiedData.getSummary())
                .build();
    }
}