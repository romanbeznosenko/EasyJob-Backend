package com.easyjob.easyjobapi.modules.applierProfile.project.models;

import lombok.Builder;

import java.util.List;

@Builder
public record ProjectPageResponse(
        long count,
        List<ProjectResponse> data
) {
}
