package com.easyjob.easyjobapi.core.offer.magenement;

import com.easyjob.easyjobapi.core.firm.models.FirmDAO;
import com.easyjob.easyjobapi.core.offer.models.OfferDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OfferManager {
    private final OfferRepository offerRepository;

    public OfferDAO saveToDatabase(OfferDAO offer) {
        return offerRepository.save(offer);
    }

    public List<OfferDAO> findByFirm(FirmDAO firm) {
        return offerRepository.findByFirm(firm);
    }

    public Optional<OfferDAO> findById(UUID id) {
        return offerRepository.findById(id);
    }

    public Page<OfferDAO> findAll(Pageable pageable) {
        return offerRepository.findAll(pageable);
    }
}