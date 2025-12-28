package com.easyjob.easyjobapi.core.offerApplication.models;

import com.easyjob.easyjobapi.core.offer.models.OfferResponse;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileResponse;
import com.easyjob.easyjobapi.utils.enums.ApplicationStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record OfferApplicationResponse(
        @Schema(description = "Offer application assigned id", example = "5d2d171f-995d-4b3a-99bc-a95f4725cfb8")
        UUID offerApplicationId,

        @Schema(description = "Offer", implementation = OfferResponse.class)
        OfferResponse offer,

        @Schema(description = "Applier profile", implementation = ApplierProfileResponse.class)
        ApplierProfileResponse applierProfile,

        @Schema(description = "Offer application status", example = "ACCEPTED")
        ApplicationStatusEnum status,

        @Schema(description = "Is opened flag", example = "false")
        Boolean isOpened,

        @Schema(description = "Creation timestamp")
        Instant createdAt
) {
}
