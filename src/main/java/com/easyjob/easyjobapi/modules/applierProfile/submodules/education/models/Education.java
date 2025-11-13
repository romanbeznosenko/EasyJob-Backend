package com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models;

import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Education {
    private EducationId educationId;
    private ApplierProfile applierProfile;
    private String degree;
    private String university;
    private LocalDate startDate;
    private LocalDate endDate;
    private String major;
    private Double gpa;

    private Instant createdAt;
    private Instant updatedAt;

    private Boolean isArchived;
    private Instant archivedAt;
}
