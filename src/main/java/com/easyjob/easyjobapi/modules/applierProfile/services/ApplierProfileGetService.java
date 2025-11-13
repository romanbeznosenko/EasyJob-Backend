package com.easyjob.easyjobapi.modules.applierProfile.services;

import com.easyjob.easyjobapi.core.user.management.UserMapper;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.core.user.models.UserResponse;
import com.easyjob.easyjobapi.modules.applierProfile.education.management.EducationManager;
import com.easyjob.easyjobapi.modules.applierProfile.education.models.EducationDAO;
import com.easyjob.easyjobapi.modules.applierProfile.education.models.EducationResponse;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileManager;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileResponse;
import com.easyjob.easyjobapi.modules.applierProfile.project.management.ProjectManager;
import com.easyjob.easyjobapi.modules.applierProfile.project.models.ProjectDAO;
import com.easyjob.easyjobapi.modules.applierProfile.project.models.ProjectResponse;
import com.easyjob.easyjobapi.modules.applierProfile.skill.management.SkillManager;
import com.easyjob.easyjobapi.modules.applierProfile.skill.models.SkillDAO;
import com.easyjob.easyjobapi.modules.applierProfile.skill.models.SkillResponse;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.management.WorkExperienceManager;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.models.WorkExperience;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.models.WorkExperienceDAO;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.models.WorkExperienceResponse;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ApplierProfileGetService {
    private final HttpServletRequest request;
    private final ApplierProfileManager applierProfileManager;
    private final EducationManager educationManager;
    private final ProjectManager projectManager;
    private final SkillManager skillManager;
    private final WorkExperienceManager workExperienceManager;
    private final UserMapper userMapper;

    public ApplierProfileResponse get() {
        log.info("Getting user applier profile");

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
                .map(item -> EducationResponse.builder()
                        .educationId(item.getId())
                        .applierProfileId(item.getApplierProfile().getId())
                        .degree(item.getDegree())
                        .university(item.getUniversity())
                        .startDate(item.getStartDate())
                        .endDate(item.getEndDate())
                        .major(item.getMajor())
                        .gpa(item.getGpa())
                        .build())
                .toList();

        List<ProjectResponse> projectResponses = projectPage.get()
                .filter(item -> item.getIsArchived() == false)
                .map(item -> ProjectResponse.builder()
                        .projectId(item.getId())
                        .applierProfileId(item.getApplierProfile().getId())
                        .name(item.getName())
                        .description(item.getDescription())
                        .technologies(item.getTechnologies())
                        .ling(item.getLink())
                        .build())
                .toList();

        List<SkillResponse> skillResponses = skillPage.get()
                .filter(item -> item.getIsArchived() == false)
                .map(item -> SkillResponse.builder()
                        .skillId(item.getId())
                        .applierProfileId(item.getApplierProfile().getId())
                        .name(item.getName())
                        .level(item.getLevel())
                        .build())
                .toList();

        List<WorkExperienceResponse> workExperienceResponses = workExperiencePage.get()
                .filter(item -> item.getIsArchived() == false)
                .map(workExperienceDAO -> WorkExperienceResponse.builder()
                        .workExperienceId(workExperienceDAO.getId())
                        .applierProfileId(workExperienceDAO.getApplierProfile().getId())
                        .title(workExperienceDAO.getTitle())
                        .companyName(workExperienceDAO.getCompanyName())
                        .startDate(workExperienceDAO.getStartDate())
                        .endDate(workExperienceDAO.getEndDate())
                        .responsibilities(workExperienceDAO.getResponsibilities())
                        .location(workExperienceDAO.getLocation())
                        .build())
                .toList();

        UserResponse userResponse = userMapper.mapToResponseFromEntity(userDAO, new CycleAvoidingMappingContext());

        return ApplierProfileResponse.builder()
                .applierProfileId(applierProfileDAO.getId())
                .user(userResponse)
                .education(educationResponses)
                .project(projectResponses)
                .skill(skillResponses)
                .workExperience(workExperienceResponses)
                .build();
    }
}
