package com.easyjob.easyjobapi.modules.applierProfile.workExperience.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Builder
public record WorkExperienceRequest(
        @NotBlank
        @Size(max = 255)
        @Schema(description = "Job title", example = "Middle Backend Developer", nullable = false)
        String title,

        @NotBlank
        @Size(max = 255)
        @Schema(description = "Company name", example = "Hitachi", nullable = true)
        String companyName,

        @NotNull
        @Schema(description = "Start date", example = "1998-05-21", nullable = true)
        LocalDate startDate,

        @NotNull
        @Schema(description = "End date", example = "2020-10-21")
        LocalDate endDate,

        @Schema(description = "Job responsiblilites", example = "Managing project timelines, coordinating communication between teams, preparing weekly reports, and optimizing workflow efficiency.")
        String responsibilities,

        @Schema(description = "Job location", example = "Lodz, Poland")
        String location
) {
}
