package com.easyjob.easyjobapi.modules.applierProfile.models;

import com.easyjob.easyjobapi.core.user.models.UserResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models.EducationResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.ProjectResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.models.SkillResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.models.WorkExperienceResponse;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ApplierProfileResponse(
        UUID applierProfileId,
        UserResponse user,
        String cv,
        List<EducationResponse> education,
        List<ProjectResponse> project,
        List<SkillResponse> skill,
        List<WorkExperienceResponse> workExperience
) {
}
