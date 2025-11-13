package com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.management;

import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.models.WorkExperienceDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkExperienceRepository extends JpaRepository<WorkExperienceDAO, UUID> {
    Page<WorkExperienceDAO> findByApplierProfile_Id(UUID applierProfileId, Pageable pageable);
}