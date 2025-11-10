package com.easyjob.easyjobapi.core.user.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserEditRequest(
        @NotNull
        @NotEmpty
        @Schema(description = "User's first name", example = "John")
        String name,

        @NotNull
        @NotEmpty
        @Schema(description = "User's surname", example = "Doe")
        String surname
) {
}

  

