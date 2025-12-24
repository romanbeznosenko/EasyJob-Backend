package com.easyjob.easyjobapi.core.offer.services;

import com.easyjob.easyjobapi.core.firm.management.FirmManager;
import com.easyjob.easyjobapi.core.firm.management.FirmMapper;
import com.easyjob.easyjobapi.core.firm.management.FirmNotFoundException;
import com.easyjob.easyjobapi.core.firm.models.Firm;
import com.easyjob.easyjobapi.core.firm.models.FirmDAO;
import com.easyjob.easyjobapi.core.offer.magenement.OfferManager;
import com.easyjob.easyjobapi.core.offer.magenement.OfferMapper;
import com.easyjob.easyjobapi.core.offer.models.Offer;
import com.easyjob.easyjobapi.core.offer.models.OfferDAO;
import com.easyjob.easyjobapi.core.offer.models.OfferRequest;
import com.easyjob.easyjobapi.core.user.management.UserNotRecruiterException;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import com.easyjob.easyjobapi.utils.enums.UserTypeEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OfferCreateService {
    private final HttpServletRequest request;
    private final FirmManager firmManager;
    private final FirmMapper firmMapper;
    private final OfferManager offerManager;
    private final OfferMapper offerMapper;

    public void create(OfferRequest offerRequest) {
        log.info("Create Offer service started");

        UserDAO userDAO = (UserDAO) request.getAttribute("user");
        if (!userDAO.getUserType().equals(UserTypeEnum.RECRUITER)) {
            throw new UserNotRecruiterException();
        }

        FirmDAO firmDAO = firmManager.findFirmByUser(userDAO)
                .orElseThrow(FirmNotFoundException::new);
        Firm firm = firmMapper.mapToDomain(firmDAO, new CycleAvoidingMappingContext());

        Offer offer = OfferBuilders.buildFromRequest(
                offerRequest,
                firm
        );
        OfferDAO offerDAO = offerMapper.mapToEntity(offer, new CycleAvoidingMappingContext());
        offerManager.saveToDatabase(offerDAO);

        log.info("Create Offer service finished");
    }
}
