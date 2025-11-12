package com.easyjob.easyjobapi.modules.applierProfile.education.services;

import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.modules.applierProfile.education.management.EducationManager;
import com.easyjob.easyjobapi.modules.applierProfile.education.management.EducationMapper;
import com.easyjob.easyjobapi.modules.applierProfile.education.models.Education;
import com.easyjob.easyjobapi.modules.applierProfile.education.models.EducationDAO;
import com.easyjob.easyjobapi.modules.applierProfile.education.models.EducationRequest;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileManager;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileMapper;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class EducationCreateService {
    private final HttpServletRequest request;
    private final EducationManager educationManager;
    private final EducationMapper educationMapper;
    private final ApplierProfileManager applierProfileManager;
    private final ApplierProfileMapper applierProfileMapper;

    public void create(EducationRequest educationRequest) {
        log.info("Creating Education");

        UserDAO userDAO = (UserDAO) request.getAttribute("user");
        ApplierProfileDAO applierProfileDAO = applierProfileManager.findByUser(userDAO.getId())
                .orElseThrow(ApplierProfileNotFoundException::new);
        ApplierProfile applierProfile = applierProfileMapper.mapToDomain(applierProfileDAO, new CycleAvoidingMappingContext());

        Education education = EducationBuilders.buildFromRequest(educationRequest, applierProfile);
        EducationDAO educationDAO = educationMapper.mapToEntity(education, new CycleAvoidingMappingContext());

        educationManager.saveToDatabase(educationDAO);
        log.info("Education Created Successfully");
    }
}
