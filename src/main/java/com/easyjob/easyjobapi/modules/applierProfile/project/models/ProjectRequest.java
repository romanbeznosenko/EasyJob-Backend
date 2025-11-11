package com.easyjob.easyjobapi.modules.applierProfile.project.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ProjectRequest(
        @NotBlank
        @Size(max = 100)
        @Schema(description = "Project name", example = "Web platform")
        String name,

        @NotBlank
        @Schema(description = "Project description", example = "This is a web platform")
        String description,

        @Schema(description = "Project technologies", example = "Python, React.js")
        String technologies,

        @Schema(description = "Link", example = "http://example.com/exmaple")
        String link
) {
}
