package com.easyjob.easyjobapi.core.offerApplicationEvaluation.services;

import com.easyjob.easyjobapi.core.firm.management.FirmOwnerMismatchException;
import com.easyjob.easyjobapi.core.offerApplication.management.OfferApplicationManager;
import com.easyjob.easyjobapi.core.offerApplication.management.OfferApplicationMapper;
import com.easyjob.easyjobapi.core.offerApplication.management.OfferApplicationNotFoundException;
import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplication;
import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplicationDAO;
import com.easyjob.easyjobapi.core.offerApplicationEvaluation.management.OfferApplicationEvaluationManager;
import com.easyjob.easyjobapi.core.offerApplicationEvaluation.management.OfferApplicationEvaluationMapper;
import com.easyjob.easyjobapi.core.offerApplicationEvaluation.models.OfferApplicationEvaluation;
import com.easyjob.easyjobapi.core.offerApplicationEvaluation.models.OfferApplicationEvaluationDAO;
import com.easyjob.easyjobapi.core.user.management.UserNotRecruiterException;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileMapper;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
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
public class OfferApplicationEvaluationEvaluateService {
    private final HttpServletRequest httpServletRequest;
    private final OfferApplicationManager offerApplicationManager;
    private final OfferApplicationMapper offerApplicationMapper;
    private final ApplierProfileMapper applierProfileMapper;
    private final OfferApplicationEvaluationManager offerApplicationEvaluationManager;
    private final OfferApplicationEvaluationMapper offerApplicationEvaluationMapper;

    public void evaluate(UUID id){
        log.info("Evaluating offer application with id: {}", id);
        UserDAO userDAO = (UserDAO) httpServletRequest.getAttribute("user");

        if (!userDAO.getUserType().equals(UserTypeEnum.RECRUITER)) {
            throw new UserNotRecruiterException();
        }

        OfferApplicationDAO offerApplicationDAO = offerApplicationManager.findById(id)
                .orElseThrow(OfferApplicationNotFoundException::new);

        if (!offerApplicationDAO.getOffer().getFirm().getUser().equals(userDAO)) {
            throw new FirmOwnerMismatchException();
        }

        OfferApplication offerApplication = offerApplicationMapper.mapToDomain(offerApplicationDAO, new CycleAvoidingMappingContext());
        ApplierProfile applierProfile = applierProfileMapper.mapToDomain(offerApplicationDAO.getApplierProfile(), new CycleAvoidingMappingContext());

        OfferApplicationEvaluation offerApplicationEvaluation = OfferApplicationEvaluationBuilders.build(offerApplication, applierProfile);
        OfferApplicationEvaluationDAO offerApplicationEvaluationDAO = offerApplicationEvaluationMapper.mapToEntity(offerApplicationEvaluation, new CycleAvoidingMappingContext());

        offerApplicationEvaluationManager.saveToDatabase(offerApplicationEvaluationDAO);
    }
}
