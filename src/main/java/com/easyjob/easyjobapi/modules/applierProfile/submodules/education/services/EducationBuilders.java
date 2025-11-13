package com.easyjob.easyjobapi.modules.applierProfile.submodules.education.services;

import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models.Education;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models.EducationId;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models.EducationRequest;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EducationBuilders {
    public static Education buildFromRequest(EducationRequest request, ApplierProfile applierProfile) {
        return Education.builder()
                .educationId(EducationId.of(null))
                .applierProfile(applierProfile)
                .degree(request.degree())
                .university(request.university())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .major(request.major())
                .gpa(request.gpa())
                .isArchived(false)
                .build();
    }
}
