package com.easyjob.easyjobapi.core.offer.models;

import com.easyjob.easyjobapi.core.firm.models.Firm;
import com.easyjob.easyjobapi.utils.enums.EmploymentTypeEnum;
import com.easyjob.easyjobapi.utils.enums.ExperienceLevelEnum;
import com.easyjob.easyjobapi.utils.enums.WorkModeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Offer {
    private OfferId offerId;
    private String name;
    private String description;
    private Firm firm;
    private String responsibilities;
    private String requirements;
    private Boolean isSalaryDisclosed;
    private Long salaryBottom;
    private Long salaryTop;
    private EmploymentTypeEnum employmentType;
    private ExperienceLevelEnum experienceLevel;
    private WorkModeEnum workMode;
    private List<String> skills;

    private Instant createdAt;
    private Instant updatedAt;

    private Boolean isArchived;
    private Instant archivedAt;
}
