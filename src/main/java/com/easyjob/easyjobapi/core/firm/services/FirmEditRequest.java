package com.easyjob.easyjobapi.core.firm.services;

import com.easyjob.easyjobapi.core.firm.management.FirmManager;
import com.easyjob.easyjobapi.core.firm.management.FirmNotFoundException;
import com.easyjob.easyjobapi.core.firm.models.FirmDAO;
import com.easyjob.easyjobapi.core.firm.models.FirmRequest;
import com.easyjob.easyjobapi.core.user.management.UserNotRecruiterException;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.utils.enums.UserTypeEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FirmEditRequest {
    private final HttpServletRequest request;
    private final FirmManager firmManager;

    public void edit(FirmRequest firmRequest) {
        log.info("Editing recruiter's firm");

        UserDAO userDAO = (UserDAO) request.getAttribute("user");
        if (!userDAO.getUserType().equals(UserTypeEnum.RECRUITER)) {
            throw new UserNotRecruiterException();
        }

        FirmDAO firmDAO = firmManager.findFirmByUser(userDAO)
                .orElseThrow(FirmNotFoundException::new);

        firmDAO.setName(firmRequest.name());
        firmDAO.setDescription(firmRequest.description());
        firmDAO.setLocation(firmRequest.location());

        firmManager.saveToDatabase(firmDAO);

        log.info("Saved recruiter's firm");
    }
}
