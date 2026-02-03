package com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.services;

import com.easyjob.easyjobapi.files.storage.services.StorageService;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CV;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CVDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CVId;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CVResponse;
import com.easyjob.easyjobapi.utils.enums.ProcessStatusEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CVBuilders {
    public static CV build(ApplierProfile applierProfile, String storageKey, String thumbnail) {
        return CV.builder()
                .cvId(CVId.of(null))
                .applierProfile(applierProfile)
                .storageKey(storageKey)
                .filename(storageKey)
                .thumbnail(thumbnail)
                .processStatus(ProcessStatusEnum.PENDING)
                .isArchived(false)
                .build();
    }

    public static CVResponse buildResponse(CVDAO cvDAO, StorageService storageService) {
        return CVResponse.builder()
                .cvId(cvDAO.getId())
                .storageKey(storageService.createPresignedGetUrl(cvDAO.getStorageKey()))
                .filename(cvDAO.getFilename())
                .thumbnail(storageService.createPresignedGetUrl(cvDAO.getThumbnail()))
                .processStatus(cvDAO.getProcessStatus())
                .createdAt(cvDAO.getCreatedAt())
                .build();
    }

    public static CV build(ApplierProfile applierProfile, String fileName, String storageKey, String thumbnail) {
        return CV.builder()
                .cvId(CVId.of(null))
                .applierProfile(applierProfile)
                .storageKey(storageKey)
                .filename(fileName)
                .thumbnail(thumbnail)
                .processStatus(ProcessStatusEnum.PENDING)
                .isArchived(false)
                .build();
    }
}
