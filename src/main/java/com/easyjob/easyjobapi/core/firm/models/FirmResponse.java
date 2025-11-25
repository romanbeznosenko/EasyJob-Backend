package com.easyjob.easyjobapi.core.firm.models;

import com.easyjob.easyjobapi.core.user.models.UserResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
public record FirmResponse(
        @Schema(description = "Firm's id", example = "5d2d171f-995d-4b3a-99bc-a95f4725cfb8")
        UUID firmId,

        @Schema(description = "Firm's name", example = "Commarch")
        String name,

        @Schema(description = "Firm's owner", implementation = UserResponse.class)
        UserResponse owner,

        @Schema(description = "Firm's description")
        String description,

        @Schema(description = "Firm's location", example = "Lodz, Poland")
        String location,

        @Schema(description = "Firm's logo", example = "https://example.com/example.png")
        String logo
) {
}
