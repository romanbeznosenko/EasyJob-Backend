package com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.management;

import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CVDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CVRepository extends JpaRepository<CVDAO, UUID> {
}