package com.easyjob.easyjobapi.modules.applierProfile.project.management;

import com.easyjob.easyjobapi.modules.applierProfile.project.models.ProjectDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectManager {
    private final ProjectRepository projectRepository;

    public ProjectDAO saveToDatabase(ProjectDAO project) {
        return projectRepository.save(project);
    }
}