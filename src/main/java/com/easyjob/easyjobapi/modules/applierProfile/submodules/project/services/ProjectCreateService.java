package com.easyjob.easyjobapi.modules.applierProfile.submodules.project.services;

import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileManager;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileMapper;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.management.ProjectManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.management.ProjectMapper;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.Project;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.ProjectDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.ProjectRequest;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProjectCreateService {
    private final HttpServletRequest request;
    private final ApplierProfileManager applierProfileManager;
    private final ApplierProfileMapper applierProfileMapper;
    private final ProjectManager projectManager;
    private final ProjectMapper projectMapper;

    public void create(ProjectRequest projectRequest) {
        log.info("Creating Project Request");

        UserDAO userDAO = (UserDAO) request.getAttribute("user");
        ApplierProfileDAO applierProfileDAO = applierProfileManager.findByUser(userDAO.getId())
                .orElseThrow(ApplierProfileNotFoundException::new);
        ApplierProfile applierProfile = applierProfileMapper.mapToDomain(applierProfileDAO, new CycleAvoidingMappingContext());

        Project project = ProjectBuilders.buildFromRequest(projectRequest, applierProfile);
        ProjectDAO projectDAO = projectMapper.mapToEntity(project, new CycleAvoidingMappingContext());

        projectManager.saveToDatabase(projectDAO);

        log.info("Project Created Successfully");
    }
}
