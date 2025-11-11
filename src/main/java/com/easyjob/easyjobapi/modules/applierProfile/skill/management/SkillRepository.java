package com.easyjob.easyjobapi.modules.applierProfile.skill.management;

import com.easyjob.easyjobapi.modules.applierProfile.skill.models.SkillDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SkillRepository extends JpaRepository<SkillDAO, UUID> {
    Page<SkillDAO> findByApplierProfile_Id(UUID applierProfileId, Pageable pageable);
}