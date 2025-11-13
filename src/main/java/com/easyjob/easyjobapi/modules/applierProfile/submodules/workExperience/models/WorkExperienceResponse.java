package com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record WorkExperienceResponse(
        @Schema(description = "Work Experience ID")
        UUID workExperienceId,

        @Schema(description = "Applier Profile ID")
        UUID applierProfileId,

        @Schema(description = "Job title", example = "Middle Backend Developer")
        String title,

        @Schema(description = "Company name", example = "Hitachi")
        String companyName,

        @Schema(description = "Job start date")
        LocalDate startDate,

        @Schema(description = "End date")
        LocalDate endDate,

        @Schema(description = "Job responsibilities")
        String responsibilities,

        @Schema(description = "Job location")
        String location
) {
}
