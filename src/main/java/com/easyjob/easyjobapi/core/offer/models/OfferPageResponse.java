package com.easyjob.easyjobapi.core.offer.models;

import lombok.Builder;

import java.util.List;

@Builder
public record OfferPageResponse(
        long count,
        List<OfferResponse> data
) {
}
