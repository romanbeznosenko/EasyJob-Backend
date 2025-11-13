package com.easyjob.easyjobapi.modules.applierProfile.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ApplierProfileCVEducationResponse(
        @Schema(description = "User education degree", example = "Bachelor")
        String degree,

        @Schema(description = "User education university", example = "MIT")
        String university,

        @Schema(description = "User education major", example = "Computer Science")
        String major,

        @Schema(description = "User education start date", example = "2022-03-01")
        @JsonProperty("start_date")
        String startDate,

        @Schema(description = "User education end date", example = "2022-03-01")
        @JsonProperty("end_date")
        String endDate,

        @Schema(description = "User education gpa", example = "4,2")
        String gpa
) {
}
