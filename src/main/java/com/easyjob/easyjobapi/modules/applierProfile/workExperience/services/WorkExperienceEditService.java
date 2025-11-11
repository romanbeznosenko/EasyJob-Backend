package com.easyjob.easyjobapi.modules.applierProfile.workExperience.services;

import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.management.WorkExperienceManager;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.management.WorkExperienceNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.management.WorkExperienceUserMismatchException;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.models.WorkExperienceDAO;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.models.WorkExperienceRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class WorkExperienceEditService {
    private final HttpServletRequest request;
    private final WorkExperienceManager workExperienceManager;

    public void edit(WorkExperienceRequest workExperienceRequest, UUID workExperienceId) throws WorkExperienceNotFoundException, WorkExperienceUserMismatchException {
        log.info("WorkExperience Request");

        log.info("Deleting workExperience with id: " + workExperienceId);

        UserDAO user = (UserDAO) request.getAttribute("user");
        WorkExperienceDAO workExperienceDAO = workExperienceManager.findByWorkExperienceId(workExperienceId)
                .orElseThrow(WorkExperienceNotFoundException::new);

        if (!workExperienceDAO.getApplierProfile().getUser().getId().equals(user.getId())) {
            throw new WorkExperienceUserMismatchException();
        }

        workExperienceDAO.setTitle(workExperienceRequest.title());
        workExperienceDAO.setCompanyName(workExperienceRequest.companyName());
        workExperienceDAO.setStartDate(workExperienceRequest.startDate());
        workExperienceDAO.setEndDate(workExperienceRequest.endDate());
        workExperienceDAO.setResponsibilities(workExperienceRequest.responsibilities());
        workExperienceDAO.setLocation(workExperienceRequest.location());

        workExperienceManager.saveToDatabase(workExperienceDAO);
        log.info("WorkExperience Saved");
    }
}
