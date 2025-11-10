package com.easyjob.easyjobapi.core.passwordReset.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record PasswordResetTokenCheckRequest(
        @NotBlank
        @Size(min = 4)
        @Schema(description = "Password reset code sent to email", example = "1273")
        String code
) {
}
