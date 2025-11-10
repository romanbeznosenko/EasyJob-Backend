package com.easyjob.easyjobapi.modules.applierProfile.models;

import com.easyjob.easyjobapi.core.user.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplierProfile {
    private ApplierProfileId applierProfileId;
    private User user;

    private Instant createdAt;
    private Instant updatedAt;

    private Boolean isArchived;
    private Instant archivedAt;
}
