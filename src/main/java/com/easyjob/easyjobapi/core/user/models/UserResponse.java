package com.easyjob.easyjobapi.core.user.models;

import com.easyjob.easyjobapi.utils.enums.UserTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponse(
        @Schema(description = "Assigned Id", example = "1")
        UUID id,

        @Schema(description = "User email", example = "example@email.com")
        String email,

        @Schema(description = "User name", example = "Kamil")
        String name,

        @Schema(description = "User surname", example = "Zdun")
        String surname,

        @Schema(description = "User type", example = "RECRUITER")
        UserTypeEnum userType,

        @Schema(description = "Information whether account is blocked", example = "true")
        Boolean blocked) {
}