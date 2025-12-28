package com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.services;

import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.management.CVManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CVDAO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CVDeleteService {
    private final HttpServletRequest request;
    private final CVManager cvManager;

    public void deleteCV(UUID id) {
        log.info("Delete CV with id: " + id);

        UserDAO userDAO = (UserDAO) request.getAttribute("user");

        CVDAO cvDAO = cvManager.findById(id).orElse(null);
        if (cvDAO != null) {
            cvDAO.setIsArchived(true);
            cvDAO.setArchivedAt(Instant.now());
            cvManager.saveToDatabase(cvDAO);
        }
    }
}
