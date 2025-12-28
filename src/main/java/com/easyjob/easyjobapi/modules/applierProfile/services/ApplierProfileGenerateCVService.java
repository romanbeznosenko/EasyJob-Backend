package com.easyjob.easyjobapi.modules.applierProfile.services;

import com.easyjob.easyjobapi.core.user.management.UserMapper;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.core.user.models.UserResponse;
import com.easyjob.easyjobapi.files.storage.services.StorageService;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.management.CVManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.management.CVMapper;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CV;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CVDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.services.CVBuilders;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.management.EducationManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.management.EducationMapper;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models.EducationDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models.EducationResponse;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileManager;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.management.ProjectManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.management.ProjectMapper;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.ProjectDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.ProjectResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.management.SkillManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.management.SkillMapper;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.models.SkillDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.models.SkillResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.management.WorkExperienceManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.management.WorkExperienceMapper;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.models.WorkExperienceDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.models.WorkExperienceResponse;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import com.easyjob.easyjobapi.utils.claude.ClaudeAIService;
import com.easyjob.easyjobapi.utils.enums.CVTemplateEnum;
import com.easyjob.easyjobapi.utils.pdf.ResumePDFService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class ApplierProfileGenerateCVService {
    private final HttpServletRequest request;
    private final ApplierProfileManager applierProfileManager;
    private final EducationManager educationManager;
    private final EducationMapper educationMapper;
    private final ProjectManager projectManager;
    private final ProjectMapper projectMapper;
    private final SkillManager skillManager;
    private final SkillMapper skillMapper;
    private final WorkExperienceManager workExperienceManager;
    private final WorkExperienceMapper workExperienceMapper;
    private final UserMapper userMapper;
    private final ClaudeAIService claudeAIService;
    private final StorageService storageService;
    private final ResumePDFService resumePDFService;
    private final CVManager cvManager;
    private final CVMapper cvMapper;

    public void generate(CVTemplateEnum template) {
        try {
            log.info("Generating CV with template: {}", template);

            if (template == null) {
                log.warn("Template is null, using default MODERN style");
                template = CVTemplateEnum.MODERN;
            }

            UserDAO userDAO = (UserDAO) request.getAttribute("user");
            ApplierProfileDAO applierProfileDAO = applierProfileManager.findByUser(userDAO.getId())
                    .orElseThrow(ApplierProfileNotFoundException::new);

            PageRequest pageable = PageRequest.of(0, Integer.MAX_VALUE);
            Page<EducationDAO> educationPage = educationManager.findByApplierProfile_Id(applierProfileDAO.getId(), pageable);
            Page<ProjectDAO> projectPage = projectManager.findByApplierProfile_Id(applierProfileDAO.getId(), pageable);
            Page<SkillDAO> skillPage = skillManager.findByApplierProfile_Id(applierProfileDAO.getId(), pageable);
            Page<WorkExperienceDAO> workExperiencePage = workExperienceManager.findByApplierProfile(applierProfileDAO.getId(), pageable);

            List<EducationResponse> educationResponses = educationPage.get()
                    .filter(item -> !item.getIsArchived())
                    .map(item -> educationMapper.mapToResponse(item, new CycleAvoidingMappingContext()))
                    .toList();

            List<ProjectResponse> projectResponses = projectPage.get()
                    .filter(item -> !item.getIsArchived())
                    .map(item -> projectMapper.mapToResponse(item, new CycleAvoidingMappingContext()))
                    .toList();

            List<SkillResponse> skillResponses = skillPage.get()
                    .filter(item -> !item.getIsArchived())
                    .map(item -> skillMapper.mapToResponse(item, new CycleAvoidingMappingContext()))
                    .toList();

            List<WorkExperienceResponse> workExperienceResponses = workExperiencePage.get()
                    .filter(item -> !item.getIsArchived())
                    .map(item -> workExperienceMapper.mapToResponse(item, new CycleAvoidingMappingContext()))
                    .toList();

            UserResponse userResponse = userMapper.mapToResponseFromEntity(userDAO, new CycleAvoidingMappingContext());

            ApplierProfileResponse applierProfileResponse = ApplierProfileResponse.builder()
                    .applierProfileId(applierProfileDAO.getId())
                    .user(userResponse)
                    .education(educationResponses)
                    .project(projectResponses)
                    .skill(skillResponses)
                    .workExperience(workExperienceResponses)
                    .build();

            String applierProfileString = ApplierProfileResponseMapper.mapToString(applierProfileResponse);

            final CVTemplateEnum selectedTemplate = template;

            CVTemplateEnum finalTemplate = template;
            claudeAIService.getApplierProfileCV(applierProfileString)
                    .thenAccept(cvData -> {
                        if (cvData != null) {
                            try {
                                log.info("Generating PDF with template: {} for user: {}",
                                        selectedTemplate, userDAO.getId());

                                byte[] cvFile = resumePDFService.generatePDF(cvData, selectedTemplate);

                                String storageKey = String.format("%s/cv/%s_%s.pdf",
                                        userDAO.getId(),
                                        selectedTemplate.name().toLowerCase(),
                                        UUID.randomUUID());

                                if (applierProfileDAO.getCv() != null) {
                                    log.info("Deleting old CV: {}", applierProfileDAO.getCv());
                                    storageService.deleteFile(applierProfileDAO.getCv());
                                }

                                applierProfileDAO.setCv(storageKey);
                                applierProfileManager.saveToDatabase(applierProfileDAO);
                                log.info("CV reference saved to database: {}", storageKey);

                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                outputStream.write(cvFile);
                                storageService.uploadFile(storageKey, "application/pdf", outputStream);

                                CV cv = CVBuilders.build(null, storageKey, null);
                                CVDAO cvDAO = cvMapper.mapToEntity(cv, new CycleAvoidingMappingContext());
                                cvDAO.setApplierProfile(applierProfileDAO);
                                cvManager.saveToDatabase(cvDAO);


                                log.info("CV generated and uploaded successfully with template: {}, size: {} bytes",
                                        selectedTemplate, cvFile.length);

                            } catch (Exception e) {
                                log.error("Failed to generate or store CV with template: {}", selectedTemplate, e);
                                throw new RuntimeException("CV generation failed: " + e.getMessage(), e);
                            }
                        } else {
                            log.warn("Claude AI returned null CV data for user: {}", userDAO.getId());
                        }
                    })
                    .exceptionally(throwable -> {
                        log.error("Error in CV generation pipeline for template: {}", selectedTemplate, throwable);
                        return null;
                    });

            log.info("CV generation process initiated successfully with template: {}", selectedTemplate);

        } catch (ApplierProfileNotFoundException e) {
            log.error("Applier profile not found for user", e);
            throw e;
        } catch (Exception e) {
            log.error("Failed to initiate CV generation with template: {}", template, e);
            throw new RuntimeException("CV generation failed: " + e.getMessage(), e);
        }
    }
}