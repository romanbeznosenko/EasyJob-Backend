package com.easyjob.easyjobapi.core.offerApplicationEvaluation.services;

import com.easyjob.easyjobapi.core.firm.management.FirmOwnerMismatchException;
import com.easyjob.easyjobapi.core.offerApplication.management.OfferApplicationManager;
import com.easyjob.easyjobapi.core.offerApplication.management.OfferApplicationNotFoundException;
import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplicationDAO;
import com.easyjob.easyjobapi.core.offerApplicationEvaluation.management.OfferApplicationEvaluationManager;
import com.easyjob.easyjobapi.core.offerApplicationEvaluation.models.OfferApplicationEvaluationDAO;
import com.easyjob.easyjobapi.core.offerApplicationEvaluation.models.OfferApplicationEvaluationResponse;
import com.easyjob.easyjobapi.core.user.management.UserNotRecruiterException;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.utils.enums.UserTypeEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OfferApplicationEvaluationGetService {
    private final HttpServletRequest httpServletRequest;
    private final OfferApplicationEvaluationManager offerApplicationEvaluationManager;
    private final OfferApplicationManager offerApplicationManager;

    public OfferApplicationEvaluationResponse getByOfferApplicationId(UUID offerApplicationId) {
        log.info("Retrieving offer application evaluation with id: {}", offerApplicationId);
        UserDAO userDAO = (UserDAO) httpServletRequest.getAttribute("user");
        if (!userDAO.getUserType().equals(UserTypeEnum.RECRUITER)) {
            throw new UserNotRecruiterException();
        }

        OfferApplicationDAO offerApplicationDAO = offerApplicationManager.findById(offerApplicationId)
                .orElseThrow(OfferApplicationNotFoundException::new);

        if (!offerApplicationDAO.getOffer().getFirm().getUser().equals(userDAO)) {
            throw new FirmOwnerMismatchException();
        }

        OfferApplicationEvaluationDAO evaluationDAO = offerApplicationEvaluationManager.findByOfferApplication(offerApplicationDAO)
                .orElseThrow(() -> new RuntimeException("Offer Application Evaluation with id: " + offerApplicationId + " not found"));

        return OfferApplicationEvaluationBuilders.buildResponse(evaluationDAO);
    }
}