package com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.services;

import com.easyjob.easyjobapi.files.storage.services.StorageService;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileManager;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileMapper;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.management.CVManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.management.CVMapper;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CV;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CVDAO;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CVUploadService {
    private final CVManager cvManager;
    private final CVMapper cvMapper;
    private final ApplierProfileManager applierProfileManager;
    private final ApplierProfileMapper applierProfileMapper;
    private final StorageService storageService;

    private final static String FOLDER_NAME = "cv";

    public void uploadCV(UUID applierProfileId, MultipartFile file) throws IOException {
        log.info("Uploading CV File to ApplierProfile with id: {}", applierProfileId);

        ApplierProfileDAO applierProfileDAO = applierProfileManager.findById(applierProfileId)
                .orElseThrow(ApplierProfileNotFoundException::new);
        ApplierProfile applierProfile = applierProfileMapper.mapToDomain(applierProfileDAO, new CycleAvoidingMappingContext());

        String storageKey = storageService.generateStorageKey(applierProfileId, file, FOLDER_NAME);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(file.getBytes());
        storageService.uploadFile(storageKey, file.getContentType(),  outputStream);

        CV cv = CVBuilders.build(applierProfile, storageKey, null);
        CVDAO cvDAO = cvMapper.mapToEntity(cv, new CycleAvoidingMappingContext());

        cvManager.saveToDatabase(cvDAO);
    }

}
