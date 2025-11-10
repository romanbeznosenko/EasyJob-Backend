package com.easyjob.easyjobapi.modules.applierProfile.skill.management;

import com.easyjob.easyjobapi.modules.applierProfile.skill.models.SkillDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SkillRepository extends JpaRepository<SkillDAO, UUID> {
}