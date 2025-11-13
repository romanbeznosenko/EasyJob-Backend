package com.easyjob.easyjobapi.modules.applierProfile.project.models;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ProjectResponse(
        UUID projectId,
        UUID applierProfileId,
        String name,
        String description,
        String technologies,
        String link
) {
}
