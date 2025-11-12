package com.easyjob.easyjobapi.modules.applierProfile.education.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EducationRequest(
        @NotBlank
        @Schema(description = "Education applier degree", example = "Bachelor")
        String degree,

        @NotBlank
        @Schema(description = "Education applier university", example = "MIT")
        String university,

        @NotNull
        @Schema(description = "Education start date", example = "2016-09-01")
        LocalDate startDate,

        @Schema(description = "Education end date", example = "2020-02-13")
        LocalDate endDate,

        @Schema(description = "Education major", example = "Computer Science")
        String major,

        @Schema(description = "Education gpa", example = "4.2")
        Double gpa
) {
}
