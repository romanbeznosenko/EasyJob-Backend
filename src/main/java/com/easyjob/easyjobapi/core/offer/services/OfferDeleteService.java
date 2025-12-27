package com.easyjob.easyjobapi.core.offer.services;

import com.easyjob.easyjobapi.core.firm.management.FirmOwnerMismatchException;
import com.easyjob.easyjobapi.core.offer.magenement.OfferManager;
import com.easyjob.easyjobapi.core.offer.magenement.OfferNotFoundException;
import com.easyjob.easyjobapi.core.offer.models.OfferDAO;
import com.easyjob.easyjobapi.core.user.management.UserNotRecruiterException;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.utils.enums.UserTypeEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OfferDeleteService {
    private final HttpServletRequest httpServletRequest;
    private final OfferManager offerManager;

    public void deleteJobOffer(UUID offerID){
        log.info("Deleting offer with id: {}", offerID);

        UserDAO userDAO = (UserDAO) httpServletRequest.getAttribute("user");
        if (!userDAO.getUserType().equals(UserTypeEnum.RECRUITER)) {
            throw new UserNotRecruiterException();
        }

        OfferDAO offerDAO = offerManager.findById(offerID)
                .orElseThrow(OfferNotFoundException::new);
        if (!offerDAO.getFirm().getUser().equals(userDAO)){
            throw new FirmOwnerMismatchException();
        }

        offerDAO.setIsArchived(true);
        offerDAO.setArchivedAt(Instant.now());
        offerManager.saveToDatabase(offerDAO);

        log.info("Deleted offer with id: {}", offerID);
    }
}
