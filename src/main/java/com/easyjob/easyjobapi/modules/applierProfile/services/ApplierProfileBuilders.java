package com.easyjob.easyjobapi.modules.applierProfile.services;

import com.easyjob.easyjobapi.core.user.models.User;
import com.easyjob.easyjobapi.core.user.services.UserBuilders;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileId;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.management.EducationManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models.EducationDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models.EducationResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.management.ProjectManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.ProjectDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.ProjectResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.management.SkillManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.models.SkillDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.models.SkillResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.management.WorkExperienceManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.models.WorkExperienceDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.models.WorkExperienceResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplierProfileBuilders {
    public static ApplierProfile buildFromUser(User user){
        return ApplierProfile.builder()
                .applierProfileId(ApplierProfileId.of(null))
                .user(user)
                .cv(null)
                .build();
    }

    public static ApplierProfileResponse buildResponse(
            ApplierProfileDAO applierProfileDAO,
            List<EducationResponse> educationResponses,
            List<ProjectResponse> projectResponses,
            List<SkillResponse> skillResponses,
            List<WorkExperienceResponse> workExperiences
    ) {
        return ApplierProfileResponse.builder()
                .applierProfileId(applierProfileDAO.getId())
                .user(UserBuilders.buildUserResponseFromUser(applierProfileDAO.getUser()))
                .cv(null)
                .education(educationResponses)
                .project(projectResponses)
                .skill(skillResponses)
                .workExperience(workExperiences)
                .build();
    }

    public static List<WorkExperienceDAO> buildWorkExperiences(ApplierProfileDAO applierProfileDAO, WorkExperienceManager workExperienceManager){
        Pageable pageable = PageRequest.of(0,  Integer.MAX_VALUE);
        Page<WorkExperienceDAO> workExperienceDAOS = workExperienceManager.findByApplierProfile(applierProfileDAO.getId(), pageable);

        return workExperienceDAOS.getContent();
    }

    public static List<EducationDAO> buildEducations(ApplierProfileDAO applierProfileDAO, EducationManager educationManager){
        Pageable pageable = PageRequest.of(0,  Integer.MAX_VALUE);
        Page<EducationDAO> educationDAOS = educationManager.findByApplierProfile_Id(applierProfileDAO.getId(), pageable);

        return educationDAOS.getContent();
    }

    public static List<SkillDAO> buildSkills(ApplierProfileDAO applierProfileDAO, SkillManager skillManager){
        Pageable pageable = PageRequest.of(0,  Integer.MAX_VALUE);
        Page<SkillDAO> skillDAOS = skillManager.findByApplierProfile_Id(applierProfileDAO.getId(), pageable);

        return skillDAOS.getContent();
    }

    public static List<ProjectDAO> buildProjects(ApplierProfileDAO applierProfileDAO, ProjectManager projectManager){
        Pageable pageable = PageRequest.of(0,  Integer.MAX_VALUE);
        Page<ProjectDAO> projectDAOS = projectManager.findByApplierProfile_Id(applierProfileDAO.getId(), pageable);

        return projectDAOS.getContent();
    }
}
