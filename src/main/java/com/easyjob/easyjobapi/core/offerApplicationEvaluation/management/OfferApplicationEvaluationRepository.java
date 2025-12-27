package com.easyjob.easyjobapi.core.offerApplicationEvaluation.management;

import com.easyjob.easyjobapi.core.offerApplicationEvaluation.models.OfferApplicationEvaluationDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OfferApplicationEvaluationRepository extends JpaRepository<OfferApplicationEvaluationDAO, UUID> {
}