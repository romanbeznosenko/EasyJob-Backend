package com.easyjob.easyjobapi.modules.applierProfile.education.services;

import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.modules.applierProfile.education.management.EducationManager;
import com.easyjob.easyjobapi.modules.applierProfile.education.models.EducationDAO;
import com.easyjob.easyjobapi.modules.applierProfile.education.models.EducationPageResponse;
import com.easyjob.easyjobapi.modules.applierProfile.education.models.EducationResponse;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileManager;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
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
public class EducationGetService {
    private final HttpServletRequest request;
    private final EducationManager educationManager;
    private final ApplierProfileManager applierProfileManager;

    public EducationPageResponse get(int limit, int page){
        log.info("Education Get Request");

        UserDAO userDAO = (UserDAO) request.getAttribute("user");
        ApplierProfileDAO applierProfileDAO = applierProfileManager.findByUser(userDAO.getId())
                .orElseThrow(ApplierProfileNotFoundException::new);

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<EducationDAO> educationDAOPage = educationManager.findByApplierProfile_Id(applierProfileDAO.getId(), pageable);
        List<EducationResponse> educationResponseList = educationDAOPage.get()
                .filter(item -> item.getIsArchived() == false)
                .map(item -> EducationResponse.builder()
                        .educationId(item.getId())
                        .applierProfileId(item.getApplierProfile().getId())
                        .degree(item.getDegree())
                        .university(item.getUniversity())
                        .startDate(item.getStartDate())
                        .endDate(item.getEndDate())
                        .major(item.getMajor())
                        .gpa(item.getGpa())
                        .build())
                .toList();

        return EducationPageResponse.builder()
                .count(educationDAOPage.getTotalElements())
                .data(educationResponseList)
                .build();
    }
}
