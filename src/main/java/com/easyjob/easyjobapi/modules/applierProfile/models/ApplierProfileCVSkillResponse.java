package com.easyjob.easyjobapi.modules.applierProfile.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ApplierProfileCVSkillResponse(
        @Schema(description = "User skill name", example = "Python")
        String name,

        @Schema(description = "User skill level", example = "Advanced")
        String level
) {
}
