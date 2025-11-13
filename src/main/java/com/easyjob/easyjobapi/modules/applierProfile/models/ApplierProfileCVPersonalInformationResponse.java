package com.easyjob.easyjobapi.modules.applierProfile.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ApplierProfileCVPersonalInformationResponse(
    @Schema(description = "User full name", example = "Jan Kowalski")
    @JsonProperty("full_name")
    String fullName,

    @Schema(description = "User e-mail", example = "example@example.com")
    String email
) {
}
