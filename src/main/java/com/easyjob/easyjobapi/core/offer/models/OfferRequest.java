package com.easyjob.easyjobapi.core.offer.models;

import com.easyjob.easyjobapi.utils.enums.EmploymentTypeEnum;
import com.easyjob.easyjobapi.utils.enums.ExperienceLevelEnum;
import com.easyjob.easyjobapi.utils.enums.WorkModeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record OfferRequest(
        @NotBlank
        @Schema(description = "Offer's name", example = "Backend developer")
        String name,

        @NotBlank
        @Schema(description = "Offer's description", example = "This is a software developer position")
        String description,

        @NotBlank
        @Schema(description = "Offer's responsibilities", example = "This is example")
        String responsibilities,

        @NotBlank
        @Schema(description = "Offer's requirements", example = "This is requirements")
        String requirements,

        @NotNull
        @Schema(description = "Is salary disclosed", example = "true")
        Boolean isSalaryDisclosed,

        @NotNull
        @Positive
        @Schema(description = "Offer's bottom salary", example = "1000")
        Long salaryBottom,

        @NotNull
        @Positive
        @Schema(description = "Offer's top salary", example = "15000")
        Long salaryTop,

        @NotNull
        @Schema(description = "Offer's employment type", example = "INTERNSHIP")
        EmploymentTypeEnum employmentType,

        @NotNull
        @Schema(description = "Offer's experience level", example = "JUNIOR")
        ExperienceLevelEnum experienceLevel,

        @NotNull
        @Schema(description = "Offer's work mode", example = "ON_SITE")
        WorkModeEnum workMode,

        @NotNull
        @Schema(description = "Offer's required skills")
        List<String> skills
) {
}
