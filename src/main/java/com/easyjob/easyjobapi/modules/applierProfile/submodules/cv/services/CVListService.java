package com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.services;

import com.easyjob.easyjobapi.core.user.management.UserNotApplierException;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.files.storage.services.StorageService;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileManager;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.management.CVManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.management.CVSpecifications;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CVDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CVPageResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CVResponse;
import com.easyjob.easyjobapi.utils.enums.UserTypeEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CVListService {
    private final HttpServletRequest request;
    private final CVManager cvManager;
    private final ApplierProfileManager applierProfileManager;
    private final StorageService storageService;

    public CVPageResponse listCV(UUID applierProfileId, int page, int limit){
        log.info("Listing CV for applierProfile with id: {}", applierProfileId);

        UserDAO userDAO = (UserDAO) request.getAttribute("user");
        if (!userDAO.getUserType().equals(UserTypeEnum.APPLIER)) {
            throw new UserNotApplierException();
        }

        ApplierProfileDAO applierProfileDAO = applierProfileManager.findByUser(userDAO.getId())
                .orElseThrow(UserNotApplierException::new);

        PageRequest pageRequest = PageRequest.of(page - 1, limit);
        Specification<CVDAO> spec = CVSpecifications.findByApplierProfile(applierProfileDAO);

        Page<CVDAO> cvdaos = cvManager.findAll(spec, pageRequest);
        List<CVResponse> responses = cvdaos.get()
                .filter(item -> item.getIsArchived() == false)
                .map(item -> CVBuilders.buildResponse(item, storageService))
                .toList();

        return CVPageResponse.builder()
                .count(cvdaos.getTotalElements())
                .data(responses)
                .build();
    }
}
