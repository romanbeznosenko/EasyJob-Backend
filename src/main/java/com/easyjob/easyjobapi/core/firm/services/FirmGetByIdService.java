package com.easyjob.easyjobapi.core.firm.services;

import com.easyjob.easyjobapi.core.firm.management.FirmManager;
import com.easyjob.easyjobapi.core.firm.management.FirmNotFoundException;
import com.easyjob.easyjobapi.core.firm.models.FirmDAO;
import com.easyjob.easyjobapi.core.firm.models.FirmResponse;
import com.easyjob.easyjobapi.core.user.management.UserMapper;
import com.easyjob.easyjobapi.files.storage.services.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FirmGetByIdService {
    private final FirmManager firmManager;
    private final StorageService storageService;
    private final UserMapper userMapper;

    public FirmResponse getFirmById(UUID id) {
        log.info("Getting firm with id: {}", id);

        FirmDAO firmDAO = firmManager.findById(id)
                .orElseThrow(FirmNotFoundException::new);

        return FirmBuilders.buildResponse(firmDAO, storageService, userMapper);
    }
}
