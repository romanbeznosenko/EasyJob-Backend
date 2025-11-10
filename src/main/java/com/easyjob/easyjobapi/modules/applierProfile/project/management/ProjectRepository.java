package com.easyjob.easyjobapi.modules.applierProfile.project.management;

import com.easyjob.easyjobapi.modules.applierProfile.project.models.ProjectDAO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectRepository extends JpaRepository<ProjectDAO, UUID> {
}