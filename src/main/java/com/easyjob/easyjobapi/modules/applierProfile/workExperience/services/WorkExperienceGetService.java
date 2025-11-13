package com.easyjob.easyjobapi.modules.applierProfile.workExperience.services;

import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileManager;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.management.WorkExperienceManager;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.management.WorkExperienceMapper;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.models.WorkExperienceDAO;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.models.WorkExperiencePageResponse;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.models.WorkExperienceResponse;
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
public class WorkExperienceGetService {
    private final HttpServletRequest request;
    private final ApplierProfileManager applierProfileManager;
    private final WorkExperienceManager workExperienceManager;
    private final WorkExperienceMapper workExperienceMapper;

    public WorkExperiencePageResponse getWorkExperiencePage(int limit, int page) {
        log.info("WorkExperienceGetService getWorkExperiencePage");

        UserDAO userDAO = (UserDAO) request.getAttribute("user");
        ApplierProfileDAO applierProfileDAO = applierProfileManager.findByUser(userDAO.getId())
                .orElseThrow(ApplierProfileNotFoundException::new);

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<WorkExperienceDAO> workExperienceDAOPage = workExperienceManager.findByApplierProfile(applierProfileDAO.getId(), pageable);
        List<WorkExperienceResponse> workExperienceResponseList = workExperienceDAOPage.get()
                .filter(item -> item.getIsArchived() == false)
                .map(item -> workExperienceMapper.mapToResponse(item, new CycleAvoidingMappingContext()))
                .toList();

        return WorkExperiencePageResponse.builder()
                .count(workExperienceDAOPage.getTotalElements())
                .data(workExperienceResponseList)
                .build();
    }
}
