package com.easyjob.easyjobapi.core.passwordReset.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import com.easyjob.easyjobapi.utils.validators.password.PasswordConstraint;

@Builder
public record PasswordResetRequest(
        @NotBlank
        @Size(min = 4)
        @Schema(description = "Password reset code sent to email", example = "3721")
        String code,

        @NotBlank
        @PasswordConstraint
        String newPassword,

        @NotBlank
        @PasswordConstraint
        String confirmPassword
) {
}
