package com.easyjob.easyjobapi.modules.applierProfile.workExperience.services;

import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileManager;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileMapper;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.management.WorkExperienceManager;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.management.WorkExperienceMapper;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.models.WorkExperience;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.models.WorkExperienceDAO;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.models.WorkExperienceRequest;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class WorkExperienceCreateService {
    private final WorkExperienceManager workExperienceManager;
    private final WorkExperienceMapper workExperienceMapper;
    private final HttpServletRequest request;
    private final ApplierProfileManager applierProfileManager;
    private final ApplierProfileMapper applierProfileMapper;

    public void create(WorkExperienceRequest workExperienceRequest) {
        log.info("Received WorkExperience Request");

        UserDAO userDAO = (UserDAO) request.getAttribute("user");
        ApplierProfileDAO applierProfileDAO = applierProfileManager.findByUser(userDAO.getId())
                .orElseThrow(ApplierProfileNotFoundException::new);
        ApplierProfile applierProfile = applierProfileMapper.mapToDomain(applierProfileDAO, new CycleAvoidingMappingContext());


        WorkExperience workExperience = WorkExperienceBuilders.buildFromRequest(workExperienceRequest, applierProfile);
        WorkExperienceDAO workExperienceDAO = workExperienceMapper.mapToEntity(workExperience, new CycleAvoidingMappingContext());
        workExperienceManager.saveToDatabase(workExperienceDAO);
        log.info("WorkExperience Saved Successfully");
    }
}
