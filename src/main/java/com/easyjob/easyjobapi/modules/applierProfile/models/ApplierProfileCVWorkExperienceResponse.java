package com.easyjob.easyjobapi.modules.applierProfile.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record ApplierProfileCVWorkExperienceResponse(
        @Schema(description = "User work title", example = "Backend Developer")
        String title,

        @Schema(description = "User work company name", example = "Hitachi")
        @JsonProperty("company_name")
        String companyName,

        @Schema(description = "User work start date", example = "2022-03-01")
        @JsonProperty("start_date")
        String startDate,

        @Schema(description = "User work end date", example = "2022-03-01")
        @JsonProperty("end_date")
        String endDate,

        @Schema(description = "User work location", example = "Warsaw, Poland")
        String location,

        @Schema(description = "User work responsibilities", example = "Developed and maintained web applications using Django and React.js")
        List<String> responsibilities
) {
}
