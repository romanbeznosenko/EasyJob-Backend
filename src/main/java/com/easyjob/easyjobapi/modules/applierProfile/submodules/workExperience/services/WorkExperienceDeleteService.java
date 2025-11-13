package com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.services;

import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.management.WorkExperienceManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.management.WorkExperienceNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.management.WorkExperienceUserMismatchException;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.models.WorkExperienceDAO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class WorkExperienceDeleteService {
    private final HttpServletRequest request;
    private final WorkExperienceManager workExperienceManager;

    public void delete(UUID workExperienceId) {
        log.info("Deleting workExperience with id: " + workExperienceId);

        UserDAO user = (UserDAO) request.getAttribute("user");
        WorkExperienceDAO workExperienceDAO = workExperienceManager.findByWorkExperienceId(workExperienceId)
                .orElseThrow(WorkExperienceNotFoundException::new);

        if (!workExperienceDAO.getApplierProfile().getUser().getId().equals(user.getId())) {
            throw new WorkExperienceUserMismatchException();
        }

        workExperienceDAO.setIsArchived(true);
        workExperienceDAO.setArchivedAt(Instant.now());
        workExperienceManager.saveToDatabase(workExperienceDAO);
        log.info("WorkExperience with id: " + workExperienceId + " has been deleted");
    }
}
