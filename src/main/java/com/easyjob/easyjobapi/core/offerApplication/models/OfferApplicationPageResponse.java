package com.easyjob.easyjobapi.core.offerApplication.models;

import lombok.Builder;

import java.util.List;

@Builder
public record OfferApplicationPageResponse(
        long count,
        List<OfferApplicationResponse> data
) {
}
