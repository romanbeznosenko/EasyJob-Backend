package com.easyjob.easyjobapi.core.firm.services;

import com.easyjob.easyjobapi.core.firm.management.FirmManager;
import com.easyjob.easyjobapi.core.firm.management.FirmSpecifications;
import com.easyjob.easyjobapi.core.firm.models.FirmDAO;
import com.easyjob.easyjobapi.core.firm.models.FirmPageResponse;
import com.easyjob.easyjobapi.core.firm.models.FirmResponse;
import com.easyjob.easyjobapi.core.user.management.UserMapper;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.files.storage.services.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FirmApplierGetService {
    private final HttpServletRequest request;
    private final FirmManager firmManager;
    private final StorageService storageService;
    private final UserMapper userMapper;

    public FirmPageResponse getFirms(int page, int limit){
        log.info("Getting firms for applier");

        UserDAO userDAO = (UserDAO) request.getAttribute("user");

        Pageable pageable = PageRequest.of(page - 1, limit);
        Specification<FirmDAO> spec = FirmSpecifications.isNotArchived();

        Page<FirmDAO> firmDAOPage = firmManager.findAll(spec, pageable);
        List<FirmResponse> firmResponses = firmDAOPage.get()
                .map(item -> FirmBuilder.buildResponse(
                        item,
                        storageService,
                        userMapper
                ))
                .toList();

        return FirmPageResponse.builder()
                .count(firmDAOPage.getTotalElements())
                .data(firmResponses)
                .build();
    }
}
