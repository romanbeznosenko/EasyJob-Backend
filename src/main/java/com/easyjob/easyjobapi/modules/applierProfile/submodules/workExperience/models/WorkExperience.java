package com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.models;

import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkExperience {
    private WorkExperienceId workExperienceId;
    private ApplierProfile applierProfile;
    private String title;
    private String companyName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String responsibilities;
    private String location;

    private Instant createdAt;
    private Instant updatedAt;

    private Boolean isArchived;
    private Instant archivedAt;
}
