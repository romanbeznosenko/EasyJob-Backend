package com.easyjob.easyjobapi.modules.applierProfile.models;

import com.easyjob.easyjobapi.core.user.models.UserResponse;
import com.easyjob.easyjobapi.modules.applierProfile.education.models.EducationResponse;
import com.easyjob.easyjobapi.modules.applierProfile.project.models.ProjectResponse;
import com.easyjob.easyjobapi.modules.applierProfile.skill.models.SkillResponse;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.models.WorkExperienceResponse;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record ApplierProfileResponse(
        UUID applierProfileId,
        UserResponse user,
        List<EducationResponse> education,
        List<ProjectResponse> project,
        List<SkillResponse> skill,
        List<WorkExperienceResponse> workExperience
) {
}
