package com.easyjob.easyjobapi.core.offer.models;

import com.easyjob.easyjobapi.core.firm.models.FirmResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
public record OfferResponse(
        @Schema(description = "Offer's id", example = "5d2d171f-995d-4b3a-99bc-a95f4725cfb8")
        UUID offerId,

        @Schema(description = "Offer's name", example = "Backend developer")
        String name,

        @Schema(description = "Offer's description", example = "This a software developer position")
        String description,

        @Schema(description = "Offer's firm", implementation = FirmResponse.class)
        FirmResponse firm
) {
}
