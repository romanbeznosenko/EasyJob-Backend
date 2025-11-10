package com.easyjob.easyjobapi.core.register.models;

import com.easyjob.easyjobapi.utils.enums.UserTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import com.easyjob.easyjobapi.utils.validators.password.PasswordConstraint;

@Builder
public record RegisterRequest(
        @NotBlank
        @Email
        @Size(max = 128)
        @Schema(description = "User email", example = "user@email.com")
        String email,

        @NotBlank
        @PasswordConstraint
        @Size(max = 72)
        @Schema(description = "User password", example = "Mariusz!Pudzianowski69")
        String password,

        @NotBlank
        @Schema(description = "User type", example = "RECRUITER")
        UserTypeEnum userType) {
}
