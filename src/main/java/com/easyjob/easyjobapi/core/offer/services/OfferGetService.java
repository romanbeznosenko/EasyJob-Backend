package com.easyjob.easyjobapi.core.offer.services;

import com.easyjob.easyjobapi.core.firm.management.FirmManager;
import com.easyjob.easyjobapi.core.firm.management.FirmNotFoundException;
import com.easyjob.easyjobapi.core.firm.models.FirmDAO;
import com.easyjob.easyjobapi.core.offer.magenement.OfferManager;
import com.easyjob.easyjobapi.core.offer.models.OfferDAO;
import com.easyjob.easyjobapi.core.offer.models.OfferPageResponse;
import com.easyjob.easyjobapi.core.offer.models.OfferResponse;
import com.easyjob.easyjobapi.core.user.management.UserMapper;
import com.easyjob.easyjobapi.core.user.management.UserNotRecruiterException;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.files.storage.services.StorageService;
import com.easyjob.easyjobapi.utils.enums.UserTypeEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OfferGetService {
    private final HttpServletRequest request;
    private final FirmManager firmManager;
    private final OfferManager offerManager;
    private final StorageService storageService;
    private final UserMapper userMapper;

    public OfferPageResponse getOffers(){
        log.info("Getting offers from firm");

        UserDAO userDAO = (UserDAO) request.getAttribute("user");
        if (userDAO.getUserType().equals(UserTypeEnum.RECRUITER)) {
            throw new UserNotRecruiterException();
        }

        FirmDAO firmDAO = firmManager.findFirmByUser(userDAO)
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
