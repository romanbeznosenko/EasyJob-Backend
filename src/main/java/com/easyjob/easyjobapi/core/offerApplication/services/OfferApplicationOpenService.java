package com.easyjob.easyjobapi.core.offerApplication.services;

import com.easyjob.easyjobapi.core.firm.management.FirmOwnerMismatchException;
import com.easyjob.easyjobapi.core.offerApplication.management.OfferApplicationManager;
import com.easyjob.easyjobapi.core.offerApplication.management.OfferApplicationNotFoundException;
import com.easyjob.easyjobapi.core.offerApplication.management.OfferApplicationRepository;
import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplicationDAO;
import com.easyjob.easyjobapi.core.user.management.UserNotRecruiterException;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.utils.enums.UserTypeEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.usertype.UserType;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OfferApplicationOpenService {
    private final HttpServletRequest httpServletRequest;
    private final OfferApplicationManager offerApplicationManager;

    public void open(UUID offerApplicationId){
        log.info("Opening offer application with id: {}", offerApplicationId);
        UserDAO userDAO = (UserDAO) httpServletRequest.getAttribute("user");
        if (!userDAO.getUserType().equals(UserTypeEnum.RECRUITER)) {
            throw new UserNotRecruiterException();
        }

        OfferApplicationDAO offerApplicationDAO = offerApplicationManager.findById(offerApplicationId)
                .orElseThrow(OfferApplicationNotFoundException::new);
        if (!offerApplicationDAO.getOffer().getFirm().getUser().equals(userDAO)) {
            throw new FirmOwnerMismatchException();
        }

        offerApplicationDAO.setIsOpened(true);
        offerApplicationManager.saveToDatabase(offerApplicationDAO);
    }
}
