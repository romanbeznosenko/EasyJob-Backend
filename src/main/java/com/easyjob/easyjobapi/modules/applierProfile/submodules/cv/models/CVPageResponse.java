package com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models;

import java.util.List;

public record CVPageResponse(
        long count,
        List<CVResponse> data
) {
}
