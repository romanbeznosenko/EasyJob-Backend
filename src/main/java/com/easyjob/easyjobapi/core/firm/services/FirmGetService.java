package com.easyjob.easyjobapi.core.firm.services;

import com.easyjob.easyjobapi.core.firm.management.FirmManager;
import com.easyjob.easyjobapi.core.firm.management.FirmNotFoundException;
import com.easyjob.easyjobapi.core.firm.models.FirmDAO;
import com.easyjob.easyjobapi.core.firm.models.FirmResponse;
import com.easyjob.easyjobapi.core.user.management.UserMapper;
import com.easyjob.easyjobapi.core.user.management.UserNotRecruiterException;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.files.storage.services.StorageService;
import com.easyjob.easyjobapi.utils.enums.UserTypeEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FirmGetService {
    private final StorageService storageService;
    private final HttpServletRequest request;
    private final UserMapper userMapper;
    private final FirmManager firmManager;

    public FirmResponse getUserFirm(){
        log.info("Getting recruiter firm");

        UserDAO userDAO = (UserDAO)request.getAttribute("user");
        if (!userDAO.getUserType().equals(UserTypeEnum.RECRUITER)){
            throw new UserNotRecruiterException();
        }

        FirmDAO firmDAO = firmManager.findFirmByUser(userDAO)
                .orElseThrow(FirmNotFoundException::new);

        return FirmBuilders.buildResponse(firmDAO, storageService, userMapper);
    }
}
