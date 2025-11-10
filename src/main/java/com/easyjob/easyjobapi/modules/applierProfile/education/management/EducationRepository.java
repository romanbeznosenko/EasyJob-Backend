package com.easyjob.easyjobapi.modules.applierProfile.education.management;

import com.easyjob.easyjobapi.modules.applierProfile.education.models.EducationDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EducationRepository extends JpaRepository<EducationDAO, UUID> {
}