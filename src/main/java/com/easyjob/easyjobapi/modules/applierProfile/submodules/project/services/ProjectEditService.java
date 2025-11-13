package com.easyjob.easyjobapi.modules.applierProfile.submodules.project.services;

import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.management.ProjectManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.management.ProjectNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.management.ProjectUserMismatchException;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.ProjectDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.ProjectRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProjectEditService {
    private final HttpServletRequest request;
    private final ProjectManager projectManager;

    public void edit(ProjectRequest projectRequest, UUID projectId) {
        log.info("Editing Project Request");

        UserDAO userDAO = (UserDAO) request.getAttribute("user");
        ProjectDAO projectDAO = projectManager.findByProjectId(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        if (!projectDAO.getApplierProfile().getUser().getId().equals(userDAO.getId())) {
            throw new ProjectUserMismatchException();
        }

        projectDAO.setName(projectRequest.name());
        projectDAO.setDescription(projectRequest.description());
        projectDAO.setTechnologies(projectRequest.technologies());
        projectDAO.setLink(projectRequest.link());

        projectManager.saveToDatabase(projectDAO);

        log.info("Project Edited Successfully");
    }
}
