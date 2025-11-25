package com.easyjob.easyjobapi.core.firm.management;

import com.easyjob.easyjobapi.core.firm.models.FirmDAO;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FirmRepository extends JpaRepository<FirmDAO, UUID> {
    Optional<FirmDAO> findFirmByUser(UserDAO user);

    Page<FirmDAO> findAll(Specification<FirmDAO> spec, Pageable pageable);
}