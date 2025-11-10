package com.easyjob.easyjobapi.core.passwordReset.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PasswordResetTokenSendRequest(
        @NotBlank
        @Email
        @Schema(description = "Email to which password reset code should be send", example = "xWeterynarzx@gmail.com")
        String email
) {
}
