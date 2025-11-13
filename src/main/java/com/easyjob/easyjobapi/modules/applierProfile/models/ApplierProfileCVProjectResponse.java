package com.easyjob.easyjobapi.modules.applierProfile.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record ApplierProfileCVProjectResponse(
        @Schema(description = "User project name", example = "Web platform")
        String name,

        @Schema(description = "User project description", example = "Developed an AI-driven parking monitoring system with dual-camera integration for intelligent car detection and automated license plate recognition, improving parking management efficiency.")
        String description,

        @Schema(description = "User project technologies")
        List<String> technologies,

        @Schema(description = "User project link", example = "http://example.com/example")
        String link
) {
}
