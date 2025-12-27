package com.easyjob.easyjobapi.core.offer.services;

import com.easyjob.easyjobapi.core.firm.models.Firm;
import com.easyjob.easyjobapi.core.firm.services.FirmBuilders;
import com.easyjob.easyjobapi.core.offer.models.*;
import com.easyjob.easyjobapi.core.user.management.UserMapper;
import com.easyjob.easyjobapi.files.storage.services.StorageService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OfferBuilders {
    public static Offer buildFromRequest(OfferRequest request, Firm firm) {
        return Offer.builder()
                .offerId(OfferId.of(null))
                .name(request.name())
                .description(request.description())
                .firm(firm)
                .build();
    }

    public static OfferResponse buildResponse(OfferDAO offer, StorageService storageService, UserMapper userMapper) {
        return OfferResponse.builder()
                .offerId(offer.getId())
                .name(offer.getName())
                .description(offer.getDescription())
                .responsibilities(offer.getResponsibilities())
                .requirements(offer.getRequirements())
                .firm(FirmBuilders.buildResponse(offer.getFirm(), storageService, userMapper))
                .build();
    }
}
