package com.easyjob.easyjobapi.core.offerApplication.management;

import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplicationDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OfferApplicationRepository extends JpaRepository<OfferApplicationDAO, UUID> {
    Page<OfferApplicationDAO> findAll(Specification<OfferApplicationDAO> spec, Pageable pageable);
}