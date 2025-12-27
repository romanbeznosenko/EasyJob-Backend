package com.easyjob.easyjobapi.core.offer.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record OfferRequest(
        @NotBlank
        @Schema(description = "Offer's name", example = "Backend developer")
        String name,

        @NotBlank
        @Schema(description = "Offer's description", example = "This is a software developer position")
        String description,

        @Schema(description = "Offer's responsibilities", example = "This is example")
        String responsibilities,

        @Schema(description = "Offer's requirements", example = "This is requirements")
        String requirements
) {
}
