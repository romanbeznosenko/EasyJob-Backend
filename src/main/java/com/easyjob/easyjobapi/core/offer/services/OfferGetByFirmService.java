package com.easyjob.easyjobapi.core.offer.services;

import com.easyjob.easyjobapi.core.firm.management.FirmManager;
import com.easyjob.easyjobapi.core.firm.management.FirmNotFoundException;
import com.easyjob.easyjobapi.core.firm.models.FirmDAO;
import com.easyjob.easyjobapi.core.offer.magenement.OfferManager;
import com.easyjob.easyjobapi.core.offer.models.OfferDAO;
import com.easyjob.easyjobapi.core.offer.models.OfferPageResponse;
import com.easyjob.easyjobapi.core.offer.models.OfferResponse;
import com.easyjob.easyjobapi.core.user.management.UserMapper;
import com.easyjob.easyjobapi.files.storage.services.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OfferGetByFirmService {
    private final OfferManager offerManager;
    private final StorageService storageService;
    private final UserMapper userMapper;
    private final FirmManager firmManager;

    public OfferPageResponse getOffersByFirm(UUID firmID) {
        log.info("Get Offers By Firm ID");

        FirmDAO firmDAO = firmManager.findById(firmID)
                .orElseThrow(FirmNotFoundException::new);
        List<OfferDAO> offerDAOS = offerManager.findByFirm(firmDAO);

        List<OfferResponse> offerResponses = offerDAOS.stream()
                .map(item -> OfferBuilders.buildResponse(
                        item,
                        storageService,
                        userMapper
                ))
                .toList();

        return OfferPageResponse.builder()
                .count(offerDAOS.size())
                .data(offerResponses)
                .build();
    }
}
