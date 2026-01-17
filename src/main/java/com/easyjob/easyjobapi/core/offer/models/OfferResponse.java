package com.easyjob.easyjobapi.core.offer.models;

import com.easyjob.easyjobapi.core.firm.models.FirmResponse;
import com.easyjob.easyjobapi.utils.enums.EmploymentTypeEnum;
import com.easyjob.easyjobapi.utils.enums.ExperienceLevelEnum;
import com.easyjob.easyjobapi.utils.enums.WorkModeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record OfferResponse(
        @Schema(description = "Offer's id", example = "5d2d171f-995d-4b3a-99bc-a95f4725cfb8")
        UUID offerId,

        @Schema(description = "Offer's name", example = "Backend developer")
        String name,

        @Schema(description = "Offer's description", example = "This a software developer position")
        String description,

        @Schema(description = "Offer's responsibilities", example = "This is example")
        String responsibilities,

        @Schema(description = "Offer's requirements", example = "This is requirements")
        String requirements,

        @Schema(description = "Offer's firm", implementation = FirmResponse.class)
        FirmResponse firm,

        @Schema(description = "Is salary disclosed", example = "false")
        Boolean isSalaryDisclosed,

        @Schema(description = "Offer's bottom salary", example = "1000")
        Long salaryBottom,

        @Schema(description = "Offer's top salary", example = "15000")
        Long salaryTop,

        @Schema(description = "Offer's employment type", example = "CONTRACT")
        EmploymentTypeEnum employmentType,

        @Schema(description = "Offer's experience level", example = "JUNIOR")
        ExperienceLevelEnum experienceLevel,

        @Schema(description = "Offer's work mode", example = "ON_SITE")
        WorkModeEnum workMode,

        @Schema(description = "Offer's required skills")
        List<String> skills
) {
}
