package com.easyjob.easyjobapi.modules.applierProfile.management;

import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ApplierProfileRepository extends JpaRepository<ApplierProfileDAO, UUID> {
    Optional<ApplierProfileDAO> findByUser_Id(UUID userId);
}