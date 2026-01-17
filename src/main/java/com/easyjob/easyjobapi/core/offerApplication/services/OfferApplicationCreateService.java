package com.easyjob.easyjobapi.core.offerApplication.services;

import com.easyjob.easyjobapi.core.offer.magenement.OfferManager;
import com.easyjob.easyjobapi.core.offer.magenement.OfferNotFoundException;
import com.easyjob.easyjobapi.core.offer.models.OfferDAO;
import com.easyjob.easyjobapi.core.offerApplication.management.OfferApplicationManager;
import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplicationDAO;
import com.easyjob.easyjobapi.core.user.management.UserNotApplierException;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileManager;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.management.CVManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.management.CVNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CVDAO;
import com.easyjob.easyjobapi.utils.enums.UserTypeEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OfferApplicationCreateService {
    private final HttpServletRequest request;
    private final OfferApplicationManager offerApplicationManager;
    private final OfferManager offerManager;
    private final ApplierProfileManager applierProfileManager;
    private final CVManager cvManager;

    public void create(UUID offerId, UUID CVId) {
        log.info("Creating Offer Application");

        UserDAO userDAO = (UserDAO) request.getAttribute("user");
        if (!userDAO.getUserType().equals(UserTypeEnum.APPLIER)) {
            throw new UserNotApplierException();
        }

        OfferDAO offerDAO = offerManager.findById(offerId)
                .orElseThrow(OfferNotFoundException::new);

        ApplierProfileDAO applierProfileDAO = applierProfileManager.findByUser(userDAO.getId())
                .orElseThrow(ApplierProfileNotFoundException::new);

        OfferApplicationDAO offerApplicationDAO = OfferApplicationBuilders.buildOfferApplicationDAO(
                offerDAO,
                applierProfileDAO
        );

        CVDAO cvDAO = cvManager.findById(CVId)
                        .orElseThrow(CVNotFoundException::new);
        if (!cvDAO.getApplierProfile().equals(applierProfileDAO)) {
            throw new CVNotFoundException();
        }

        offerApplicationDAO.setCv(cvDAO.getStorageKey());

        offerApplicationManager.saveToDatabase(offerApplicationDAO);
    }
}
