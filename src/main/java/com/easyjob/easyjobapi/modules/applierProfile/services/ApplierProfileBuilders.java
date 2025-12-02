package com.easyjob.easyjobapi.modules.applierProfile.services;

import com.easyjob.easyjobapi.core.user.models.User;
import com.easyjob.easyjobapi.core.user.services.UserBuilders;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileId;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models.EducationResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.ProjectResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.models.SkillResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.models.WorkExperienceResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
}
