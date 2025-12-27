package com.easyjob.easyjobapi.core.offer.services;

import com.easyjob.easyjobapi.core.offer.magenement.OfferManager;
import com.easyjob.easyjobapi.core.offer.models.OfferDAO;
import com.easyjob.easyjobapi.core.offer.models.OfferPageResponse;
import com.easyjob.easyjobapi.core.offer.models.OfferResponse;
import com.easyjob.easyjobapi.core.user.management.UserMapper;
import com.easyjob.easyjobapi.files.storage.services.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OfferGetAllService {
    private final OfferManager offerManager;
    private final StorageService storageService;
    private final UserMapper userMapper;

    public OfferPageResponse getAllOffers(int page, int limit){
        log.info("Get All Offers");

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<OfferDAO> offerDAOS = offerManager.findAll(pageable);

        List<OfferResponse> offerResponses = offerDAOS.get()
                .filter(item -> item.getIsArchived() == false)
                .map(item -> OfferBuilders.buildResponse(
                        item,
                        storageService,
                        userMapper
                ))
                .toList();

        return OfferPageResponse.builder()
                .count(offerDAOS.getTotalElements())
                .data(offerResponses)
                .build();
    }
}
