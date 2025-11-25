package com.easyjob.easyjobapi.core.offer.magenement;

import com.easyjob.easyjobapi.core.firm.models.FirmDAO;
import com.easyjob.easyjobapi.core.offer.models.OfferDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OfferRepository extends JpaRepository<OfferDAO, UUID> {
    List<OfferDAO> findByFirm(FirmDAO firm);
}