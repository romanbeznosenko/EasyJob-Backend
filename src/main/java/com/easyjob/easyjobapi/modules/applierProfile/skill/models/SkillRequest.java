package com.easyjob.easyjobapi.modules.applierProfile.skill.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SkillRequest(
        @NotBlank
        @Schema(description = "Skill name", example = "Python")
        String name,

        @Schema(description = "Skill level", example = "Advanced")
        String level
) {
}
