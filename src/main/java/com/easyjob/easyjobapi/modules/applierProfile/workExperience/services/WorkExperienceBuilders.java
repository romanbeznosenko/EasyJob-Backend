package com.easyjob.easyjobapi.modules.applierProfile.workExperience.services;

import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.models.WorkExperience;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.models.WorkExperienceId;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.models.WorkExperienceRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkExperienceBuilders {
    public static WorkExperience buildFromRequest(WorkExperienceRequest request, ApplierProfile applierProfile) {
        return WorkExperience.builder()
                .workExperienceId(WorkExperienceId.of(null))
                .applierProfile(applierProfile)
                .title(request.title())
                .companyName(request.companyName())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .responsibilities(request.responsibilities())
                .location(request.location())
                .isArchived(false)
                .build();
    }
}
