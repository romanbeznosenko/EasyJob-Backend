package com.easyjob.easyjobapi.core.user.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserDeleteRequest(
        @Schema(description = "User password", example = "Password123@")
        @NotBlank
        String password
) {
}
