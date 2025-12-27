package com.easyjob.easyjobapi.core.offerApplicationEvaluation.management;

import com.easyjob.easyjobapi.core.offerApplicationEvaluation.models.OfferApplicationEvaluationDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OfferApplicationEvaluationManager {
    private final OfferApplicationEvaluationRepository offerApplicationEvaluationRepository;

    public OfferApplicationEvaluationDAO saveToDatabase(OfferApplicationEvaluationDAO offerApplicationEvaluation) {
        return offerApplicationEvaluationRepository.save(offerApplicationEvaluation);
    }
}