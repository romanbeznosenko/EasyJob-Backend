package com.easyjob.easyjobapi.core.firm.services;

import com.easyjob.easyjobapi.core.firm.management.FirmManager;
import com.easyjob.easyjobapi.core.firm.management.FirmMapper;
import com.easyjob.easyjobapi.core.firm.models.Firm;
import com.easyjob.easyjobapi.core.firm.models.FirmDAO;
import com.easyjob.easyjobapi.core.firm.models.FirmRequest;
import com.easyjob.easyjobapi.core.user.management.UserAlreadyHasFirmException;
import com.easyjob.easyjobapi.core.user.management.UserMapper;
import com.easyjob.easyjobapi.core.user.management.UserNotRecruiterException;
import com.easyjob.easyjobapi.core.user.models.User;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import com.easyjob.easyjobapi.utils.enums.UserTypeEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FirmCreateService {
    private final HttpServletRequest request;
    private final FirmManager firmManager;
    private final FirmMapper firmMapper;
    private final UserMapper userMapper;

    public void create(FirmRequest firmRequest) {
        log.info("Create Firm Request");

        UserDAO userDAO = (UserDAO) request.getAttribute("user");
        if (!userDAO.getUserType().equals(UserTypeEnum.RECRUITER)) {
            throw new UserNotRecruiterException();
        }

        FirmDAO firmDAO = firmManager.findFirmByUser(userDAO)
                .orElse(null);
        if (firmDAO != null) {
            throw new UserAlreadyHasFirmException();
        }

        User user = userMapper.mapToDomain(userDAO, new CycleAvoidingMappingContext());
        Firm firm = FirmBuilders.buildFromRequest(firmRequest, user);
        firmDAO = firmMapper.mapToEntity(firm, new CycleAvoidingMappingContext());
        firmManager.saveToDatabase(firmDAO);
    }
}
