package com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CVEditRequest(
        @NotBlank
        @Schema(description = "File's name", example = "CV_Jan_Kowalski")
        String filename
) {
}
