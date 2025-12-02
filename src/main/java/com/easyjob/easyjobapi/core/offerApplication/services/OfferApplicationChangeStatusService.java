package com.easyjob.easyjobapi.core.offerApplication.services;

import com.easyjob.easyjobapi.core.firm.management.FirmOwnerMismatchException;
import com.easyjob.easyjobapi.core.offer.magenement.OfferManager;
import com.easyjob.easyjobapi.core.offerApplication.management.OfferApplicationManager;
import com.easyjob.easyjobapi.core.offerApplication.management.OfferApplicationNotFoundException;
import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplicationDAO;
import com.easyjob.easyjobapi.core.user.management.UserNotRecruiterException;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.utils.enums.ApplicationStatusEnum;
import com.easyjob.easyjobapi.utils.enums.UserTypeEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OfferApplicationChangeStatusService {
    private final HttpServletRequest request;
    private final OfferApplicationManager offerApplicationManager;
    private final OfferManager offerManager;

    public void changeStatus(UUID offerApplicationId, ApplicationStatusEnum status){
        log.info("Changing offer application status with id: {}", offerApplicationId);

        UserDAO userDAO = (UserDAO) request.getAttribute("user");
        if (userDAO.getUserType().equals(UserTypeEnum.RECRUITER)) {
            throw new UserNotRecruiterException();
        }

        OfferApplicationDAO offerApplicationDAO = offerApplicationManager.findById(offerApplicationId)
                .orElseThrow(OfferApplicationNotFoundException::new);

        if (!offerApplicationDAO.getOffer().getFirm().getUser().equals(userDAO)) {
            throw new FirmOwnerMismatchException();
        }

        offerApplicationDAO.setStatus(status);
        offerApplicationManager.saveToDatabase(offerApplicationDAO);

        log.info("Offer application status successfully changed");
    }
}
