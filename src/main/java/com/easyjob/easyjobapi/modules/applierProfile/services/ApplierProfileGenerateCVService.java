package com.easyjob.easyjobapi.modules.applierProfile.services;

import com.easyjob.easyjobapi.core.user.management.UserMapper;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.core.user.models.UserResponse;
import com.easyjob.easyjobapi.files.storage.services.StorageService;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.management.EducationManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.management.EducationMapper;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models.EducationDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models.EducationResponse;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileManager;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileCVResponse;
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
import com.easyjob.easyjobapi.utils.pdf.ResumePdfGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    private final ResumePdfGenerator resumePdfGenerator;
    private final StorageService storageService;

    public String generate() throws IOException {
        log.info("Generating CV based on applier profile");

        UserDAO userDAO = (UserDAO) request.getAttribute("user");
        ApplierProfileDAO applierProfileDAO = applierProfileManager.findByUser(userDAO.getId())
                .orElseThrow(ApplierProfileNotFoundException::new);

        PageRequest pageable = PageRequest.of(0, Integer.MAX_VALUE);
        Page<EducationDAO> educationPage = educationManager.findByApplierProfile_Id(applierProfileDAO.getId(), pageable);
        Page<ProjectDAO> projectPage = projectManager.findByApplierProfile_Id(applierProfileDAO.getId(), pageable);
        Page<SkillDAO> skillPage = skillManager.findByApplierProfile_Id(applierProfileDAO.getId(), pageable);
        Page<WorkExperienceDAO> workExperiencePage = workExperienceManager.findByApplierProfile(applierProfileDAO.getId(), pageable);

        List<EducationResponse> educationResponses = educationPage.get()
                .filter(item -> item.getIsArchived() == false)
                .map(item -> educationMapper.mapToResponse(item, new CycleAvoidingMappingContext()))
                .toList();

        List<ProjectResponse> projectResponses = projectPage.get()
                .filter(item -> item.getIsArchived() == false)
                .map(item -> projectMapper.mapToResponse(item, new CycleAvoidingMappingContext()))
                .toList();

        List<SkillResponse> skillResponses = skillPage.get()
                .filter(item -> item.getIsArchived() == false)
                .map(item -> skillMapper.mapToResponse(item, new CycleAvoidingMappingContext()))
                .toList();

        List<WorkExperienceResponse> workExperienceResponses = workExperiencePage.get()
                .filter(item -> item.getIsArchived() == false)
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
        ApplierProfileCVResponse CVData = claudeAIService.getApplierProfileCV(applierProfileString);

        byte[] CVFile = resumePdfGenerator.generateResumePdf(CVData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(CVFile, 0, CVFile.length);

        String storageKey = "%s/logo/%s.pdf".formatted(userDAO.getId(), UUID.randomUUID());
        storageService.uploadFile(storageKey, "application/pdf", outputStream);

        return storageService.createPresignedGetUrl(storageKey);
    }
}
