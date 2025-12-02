package com.easyjob.easyjobapi.core.offerApplication.models;

import com.easyjob.easyjobapi.core.offer.models.Offer;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import com.easyjob.easyjobapi.utils.enums.ApplicationStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferApplication {
    private OfferApplicationId offerApplicationId;
    private Offer offer;
    private ApplierProfile applierProfile;
    private ApplicationStatusEnum status;

    private Instant createdAt;
    private Instant updatedAt;

    private Boolean isArchived;
    private Instant archivedAt;
}
