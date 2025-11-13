package com.easyjob.easyjobapi.modules.applierProfile.submodules.education.management;

import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models.EducationDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EducationRepository extends JpaRepository<EducationDAO, UUID> {
    Page<EducationDAO> findByApplierProfile_Id(UUID applierProfileId, Pageable pageable);
}