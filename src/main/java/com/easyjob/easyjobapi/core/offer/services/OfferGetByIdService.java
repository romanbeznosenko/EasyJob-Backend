package com.easyjob.easyjobapi.core.offer.services;

import com.easyjob.easyjobapi.core.offer.magenement.OfferManager;
import com.easyjob.easyjobapi.core.offer.magenement.OfferNotFoundException;
import com.easyjob.easyjobapi.core.offer.models.OfferDAO;
import com.easyjob.easyjobapi.core.offer.models.OfferResponse;
import com.easyjob.easyjobapi.core.user.management.UserMapper;
import com.easyjob.easyjobapi.files.storage.services.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OfferGetByIdService {
    private final OfferManager offerManager;
    private final StorageService storageService;
    private final UserMapper userMapper;

    public OfferResponse getOfferById(UUID offerId){
        log.info("Getting offer by id");

        OfferDAO offerDAO = offerManager.findById(offerId)
                .orElseThrow(OfferNotFoundException::new);

        if (offerDAO.getIsArchived() == false){
            throw new OfferNotFoundException();
        }

        return OfferBuilders.buildResponse(
                offerDAO,
                storageService,
                userMapper
        );
    }
}
