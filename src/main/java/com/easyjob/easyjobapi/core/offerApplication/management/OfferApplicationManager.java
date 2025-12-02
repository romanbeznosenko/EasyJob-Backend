package com.easyjob.easyjobapi.core.offerApplication.management;

import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplicationDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfferApplicationManager {
    private final OfferApplicationRepository offerApplicationRepository;

    public OfferApplicationDAO saveToDatabase(OfferApplicationDAO offerApplication) {
        return offerApplicationRepository.save(offerApplication);
    }

    public Page<OfferApplicationDAO> findAll(Specification<OfferApplicationDAO> spec, Pageable pageable) {
        return offerApplicationRepository.findAll(spec, pageable);
    }

    public Optional<OfferApplicationDAO> findById(UUID id) {
        return offerApplicationRepository.findById(id);
    }
}