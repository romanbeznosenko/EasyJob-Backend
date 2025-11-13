package com.easyjob.easyjobapi.modules.applierProfile.submodules.project.management;

import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.ProjectDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectManager {
    private final ProjectRepository projectRepository;

    public ProjectDAO saveToDatabase(ProjectDAO project) {
        return projectRepository.save(project);
    }

    public Optional<ProjectDAO> findByProjectId(UUID projectId) {
        return projectRepository.findById(projectId);
    }

    public Page<ProjectDAO> findByApplierProfile_Id(UUID applierProfileId, Pageable pageable) {
        return projectRepository.findByApplierProfile_Id(applierProfileId, pageable);
    }
}