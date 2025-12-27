package com.easyjob.easyjobapi.core.offerApplicationEvaluation.management;

import com.easyjob.easyjobapi.core.offerApplicationEvaluation.models.OfferApplicationEvaluationDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OfferApplicationEvaluationRepository extends JpaRepository<OfferApplicationEvaluationDAO, UUID> {
    @Query("SELECT e FROM OfferApplicationEvaluationDAO e WHERE e.offer.id = :offerId AND e.applierProfile.id = :applierProfileId AND e.isArchived = false")
    Optional<OfferApplicationEvaluationDAO> findByOfferAndApplierProfile(
            @Param("offerId") UUID offerId,
            @Param("applierProfileId") UUID applierProfileId
    );
}