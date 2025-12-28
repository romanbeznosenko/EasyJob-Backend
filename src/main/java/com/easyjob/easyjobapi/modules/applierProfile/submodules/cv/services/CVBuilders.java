package com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.services;

import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CV;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CVId;
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
                .isArchived(false)
                .build();
    }
}
