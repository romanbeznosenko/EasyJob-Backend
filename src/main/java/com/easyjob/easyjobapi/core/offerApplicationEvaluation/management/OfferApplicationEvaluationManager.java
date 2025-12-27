package com.easyjob.easyjobapi.core.offerApplicationEvaluation.management;

import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplicationDAO;
import com.easyjob.easyjobapi.core.offerApplicationEvaluation.models.OfferApplicationEvaluationDAO;
import com.easyjob.easyjobapi.core.offerApplicationEvaluation.models.OfferApplicationEvaluationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfferApplicationEvaluationManager {
    private final OfferApplicationEvaluationRepository offerApplicationEvaluationRepository;

    public OfferApplicationEvaluationDAO saveToDatabase(OfferApplicationEvaluationDAO offerApplicationEvaluation) {
        return offerApplicationEvaluationRepository.save(offerApplicationEvaluation);
    }

    public Optional<OfferApplicationEvaluationDAO> findByOfferAndApplierProfile(UUID offerId, UUID applierProfileId) {
        return offerApplicationEvaluationRepository.findByOfferAndApplierProfile(offerId, applierProfileId);
    }

    public Optional<OfferApplicationEvaluationDAO> findById(UUID id) {
        return offerApplicationEvaluationRepository.findById(id);
    }

    public Optional<OfferApplicationEvaluationDAO> findByOfferApplication(OfferApplicationDAO offerApplication) {
        return offerApplicationEvaluationRepository.findByOfferApplication(offerApplication);
    }
}