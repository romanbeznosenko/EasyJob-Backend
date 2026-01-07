package com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.services;

import com.easyjob.easyjobapi.core.user.management.UserNotApplierException;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.management.CVManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CVDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CVEditRequest;
import com.easyjob.easyjobapi.utils.enums.UserTypeEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CVEditService {
    private final HttpServletRequest httpServletRequest;
    private final CVManager cvManager;

    public void edit(UUID cvId, CVEditRequest editRequest) {
        log.info("Editing CV with id: {}", cvId);

        UserDAO userDAO = (UserDAO) httpServletRequest.getAttribute("user");
        if (!userDAO.getUserType().equals(UserTypeEnum.APPLIER)) {
            throw new UserNotApplierException();
        }

        CVDAO cvDAO = cvManager.findById(cvId).orElse(null);
        if (cvDAO != null) {
            cvDAO.setFilename(editRequest.filename());
            cvManager.saveToDatabase(cvDAO);
        }
    }
}
