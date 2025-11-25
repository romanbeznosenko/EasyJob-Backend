package com.easyjob.easyjobapi.core.offer.models;

import com.easyjob.easyjobapi.core.firm.models.Firm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Offer {
    private OfferId offerId;
    private String name;
    private String description;
    private Firm firm;

    private Instant createdAt;
    private Instant updatedAt;

    private Boolean isArchived;
    private Instant archivedAt;
}
