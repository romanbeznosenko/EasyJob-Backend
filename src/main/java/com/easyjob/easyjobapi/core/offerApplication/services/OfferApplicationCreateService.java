package com.easyjob.easyjobapi.core.offerApplication.services;

import com.easyjob.easyjobapi.core.offer.magenement.OfferManager;
import com.easyjob.easyjobapi.core.offer.magenement.OfferMapper;
import com.easyjob.easyjobapi.core.offer.magenement.OfferNotFoundException;
import com.easyjob.easyjobapi.core.offer.models.Offer;
import com.easyjob.easyjobapi.core.offer.models.OfferDAO;
import com.easyjob.easyjobapi.core.offerApplication.management.OfferApplicationManager;
import com.easyjob.easyjobapi.core.offerApplication.management.OfferApplicationMapper;
import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplication;
import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplicationDAO;
import com.easyjob.easyjobapi.core.user.management.UserNotApplierException;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileManager;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileMapper;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
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
    private final OfferApplicationMapper offerApplicationMapper;
    private final OfferManager offerManager;
    private final OfferMapper offerMapper;
    private final ApplierProfileManager applierProfileManager;
    private final ApplierProfileMapper applierProfileMapper;

    public void create(UUID offerId){
        log.info("Creating Offer Application");

        UserDAO userDAO = (UserDAO) request.getAttribute("user");
        if (!userDAO.getUserType().equals(UserTypeEnum.APPLIER)) {
            throw new UserNotApplierException();
        }

        OfferDAO offerDAO = offerManager.findById(offerId)
                .orElseThrow(OfferNotFoundException::new);
        Offer offer = offerMapper.mapToDomain(offerDAO, new CycleAvoidingMappingContext());

        ApplierProfileDAO applierProfileDAO = applierProfileManager.findByUser(userDAO.getId())
                .orElseThrow(ApplierProfileNotFoundException::new);
        ApplierProfile applierProfile = applierProfileMapper.mapToDomain(applierProfileDAO, new CycleAvoidingMappingContext());

        OfferApplication offerApplication = OfferApplicationBuilders.buildOfferApplication(
                offer,
                applierProfile
        );
        OfferApplicationDAO offerApplicationDAO = offerApplicationMapper.mapToEntity(offerApplication, new CycleAvoidingMappingContext());

        offerApplicationManager.saveToDatabase(offerApplicationDAO);
    }
}
