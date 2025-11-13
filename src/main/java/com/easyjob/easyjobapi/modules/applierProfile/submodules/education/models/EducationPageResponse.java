package com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models;

import lombok.Builder;

import java.util.List;

@Builder
public record EducationPageResponse(
        long count,
        List<EducationResponse> data
) {
}
