package com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models;

import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CV {
    private CVId cvId;
    private ApplierProfile applierProfile;
    private String storageKey;
    private String filename;
    private String thumbnail;

    private Instant createdAt;
    private Instant updatedAt;

    private Boolean isArchived;
    private Instant archivedAt;
}
