package com.easyjob.easyjobapi.modules.applierProfile.education.services;

import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.modules.applierProfile.education.management.EducationManager;
import com.easyjob.easyjobapi.modules.applierProfile.education.management.EducationNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.education.management.EducationUserMismatchException;
import com.easyjob.easyjobapi.modules.applierProfile.education.models.EducationDAO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class EducationDeleteService {
    private final HttpServletRequest request;
    private final EducationManager educationManager;

    public void delete(UUID educationId) {
        log.info("Delete Education with id: {}", educationId);

        UserDAO userDAO = (UserDAO) request.getAttribute("user");

        EducationDAO educationDAO = educationManager.findByEducationId(educationId)
                .orElseThrow(EducationNotFoundException::new);
        if (!educationDAO.getApplierProfile().getUser().getId().equals(userDAO.getId())) {
            throw new EducationUserMismatchException();
        }

        educationDAO.setIsArchived(true);
        educationDAO.setArchivedAt(Instant.now());

        educationManager.saveToDatabase(educationDAO);

        log.info("Deleted successfully Education with id: {}", educationId);
    }
}
