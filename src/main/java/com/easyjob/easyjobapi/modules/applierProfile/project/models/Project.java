package com.easyjob.easyjobapi.modules.applierProfile.project.models;

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
public class Project {
    private ProjectId projectId;
    private ApplierProfile applierProfile;
    private String name;
    private String description;
    private String technologies;
    private String link;

    private Instant createdAt;
    private Instant updatedAt;

    private Boolean isArchived;
    private Instant archivedAt;
}
