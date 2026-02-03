package com.easyjob.easyjobapi.core.offerApplication.models;

import com.easyjob.easyjobapi.utils.enums.CVTemplateEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OfferApplicationModifyCVRequest(
        @NotBlank
        @Schema(description = "CV's id", example = "1")
        UUID cvId,

        @NotBlank
        @Schema(description = "Modified CV name", example = "CV modified 1")
        String name,

        @NotNull
        @Schema(description = "Modified CV template")
        CVTemplateEnum cvTemplate
) {
}
