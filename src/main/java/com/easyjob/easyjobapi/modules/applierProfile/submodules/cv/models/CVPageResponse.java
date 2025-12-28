package com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models;

import lombok.Builder;

import java.util.List;

@Builder
public record CVPageResponse(
        long count,
        List<CVResponse> data
) {
}
