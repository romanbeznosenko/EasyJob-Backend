package com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.models;

import lombok.Builder;

import java.util.List;

@Builder
public record WorkExperiencePageResponse(
        long count,
        List<WorkExperienceResponse> data
) {
}
