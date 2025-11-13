package com.easyjob.easyjobapi.modules.applierProfile.submodules.project.services;

import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileManager;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.management.ProjectManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.management.ProjectMapper;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.ProjectDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.ProjectPageResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.ProjectResponse;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ProjectGetService {
    private final HttpServletRequest request;
    private final ProjectManager projectManager;
    private final ProjectMapper projectMapper;
    private final ApplierProfileManager applierProfileManager;

    public ProjectPageResponse get(int limit, int page){
        log.info("Inside get ProjectGetService");

        UserDAO userDAO = (UserDAO) request.getAttribute("user");
        ApplierProfileDAO applierProfileDAO = applierProfileManager.findByUser(userDAO.getId())
                .orElseThrow(ApplierProfileNotFoundException::new);

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<ProjectDAO>  projectPage = projectManager.findByApplierProfile_Id(applierProfileDAO.getId(), pageable);
        List<ProjectResponse> projectResponses = projectPage.get()
                .filter(item -> item.getIsArchived() == false)
                .map(item -> projectMapper.mapToResponse(item, new CycleAvoidingMappingContext()))
                .toList();

        return ProjectPageResponse.builder()
                .count(projectPage.getTotalElements())
                .data(projectResponses)
                .build();
    }
}
