package com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models;

import com.easyjob.easyjobapi.utils.enums.ProcessStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record CVResponse(
        @Schema(description = "Assigned id", example = "1")
        UUID cvId,

        @Schema(description = "CV link", example = "https://example.com/example.png")
        String storageKey,

        @Schema(description = "Filename", example = "CV Modern")
        String filename,

        @Schema(description = "Thumbnail link", example = "https://example.com/example.png")
        String thumbnail,

        @Schema(description = "Process status", example = "COMPLETED")
        ProcessStatusEnum processStatus,

        @Schema(description = "Creation timestamp")
        Instant createdAt
) {
}
