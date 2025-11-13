package com.easyjob.easyjobapi.modules.applierProfile.submodules.project.services;

import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.management.ProjectManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.management.ProjectNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.management.ProjectUserMismatchException;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.ProjectDAO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProjectDeleteService {
    private final HttpServletRequest request;
    private final ProjectManager projectManager;

    public void delete(UUID projectId) {
        log.info("Delete Project with id: {}", projectId);

        UserDAO userDAO = (UserDAO) request.getAttribute("user");
        ProjectDAO projectDAO = projectManager.findByProjectId(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        if (!projectDAO.getApplierProfile().getUser().getId().equals(userDAO.getId())) {
            throw new ProjectUserMismatchException();
        }

        projectDAO.setIsArchived(true);
        projectDAO.setArchivedAt(Instant.now());

        projectManager.saveToDatabase(projectDAO);
        log.info("Project Deleted successfully");
    }
}
