package com.easyjob.easyjobapi.core.firm.models;

import lombok.Builder;

import java.util.List;

@Builder
public record FirmPageResponse(
        long count,
        List<FirmResponse> data
) {
}
