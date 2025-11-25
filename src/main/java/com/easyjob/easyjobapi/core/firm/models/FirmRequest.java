package com.easyjob.easyjobapi.core.firm.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record FirmRequest(
        @NotBlank
        @Schema(description = "Firm's name", example = "Commarch")
        String name,

        @NotBlank
        @Schema(description = "Firm's description", example = "This a sfotware firm")
        String description,

        @NotBlank
        @Schema(description = "Firm's location", example = "Lodz, Poland")
        String location
) {
}
