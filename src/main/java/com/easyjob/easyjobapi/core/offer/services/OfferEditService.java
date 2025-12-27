package com.easyjob.easyjobapi.core.offer.services;

import com.easyjob.easyjobapi.core.firm.management.FirmManager;
import com.easyjob.easyjobapi.core.firm.management.FirmNotFoundException;
import com.easyjob.easyjobapi.core.firm.models.FirmDAO;
import com.easyjob.easyjobapi.core.offer.magenement.OfferManager;
import com.easyjob.easyjobapi.core.offer.magenement.OfferNotFoundException;
import com.easyjob.easyjobapi.core.offer.models.OfferDAO;
import com.easyjob.easyjobapi.core.offer.models.OfferRequest;
import com.easyjob.easyjobapi.core.user.management.UserNotRecruiterException;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OfferEditService {
    private final HttpServletRequest request;
    private final FirmManager firmManager;
    private final OfferManager offerManager;

    public void edit(UUID offerId, OfferRequest offerRequest){
        log.info("Editing offer with id: {}", offerId);

        UserDAO userDAO = (UserDAO) request.getAttribute("user");
        FirmDAO firmDAO = firmManager.findFirmByUser(userDAO)
                .orElseThrow(FirmNotFoundException::new);
        if (!firmDAO.getUser().equals(userDAO)){
            throw new UserNotRecruiterException();
        }

        OfferDAO offerDAO = offerManager.findById(offerId)
                .orElseThrow(OfferNotFoundException::new);

        offerDAO.setName(offerRequest.name());
        offerDAO.setDescription(offerRequest.description());
        offerDAO.setRequirements(offerRequest.requirements());
        offerDAO.setResponsibilities(offerRequest.responsibilities());

        offerManager.saveToDatabase(offerDAO);

        log.info("Saved offer with id: {}", offerId);
    }
}
