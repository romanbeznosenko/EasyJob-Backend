package com.easyjob.easyjobapi.modules.applierProfile.submodules.education.services;

import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.management.EducationManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.management.EducationNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.management.EducationUserMismatchException;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models.EducationDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models.EducationRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class EducationEditService {
    private final HttpServletRequest request;
    private final EducationManager educationManager;

    public void edit(EducationRequest educationRequest, UUID educationId) {
        log.info("Editing Education with id: {}", educationId);

        UserDAO userDAO = (UserDAO) request.getAttribute("user");

        EducationDAO educationDAO = educationManager.findByEducationId(educationId)
                .orElseThrow(EducationNotFoundException::new);
        if (!educationDAO.getApplierProfile().getUser().getId().equals(userDAO.getId())) {
            throw new EducationUserMismatchException();
        }

        educationDAO.setDegree(educationRequest.degree());
        educationDAO.setUniversity(educationRequest.university());
        educationDAO.setStartDate(educationRequest.startDate());
        educationDAO.setEndDate(educationRequest.endDate());
        educationDAO.setMajor(educationRequest.major());
        educationDAO.setGpa(educationRequest.gpa());

        educationManager.saveToDatabase(educationDAO);

        log.info("Education Edited Successfully");
    }
}
