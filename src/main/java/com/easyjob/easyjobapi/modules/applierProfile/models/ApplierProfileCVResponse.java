package com.easyjob.easyjobapi.modules.applierProfile.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record ApplierProfileCVResponse(
        @JsonProperty("personal_information")
        ApplierProfileCVPersonalInformationResponse personalInformation,

        List<ApplierProfileCVEducationResponse> education,

        @JsonProperty("work_experience")
        List<ApplierProfileCVWorkExperienceResponse> workExperience,

        List<ApplierProfileCVProjectResponse> projects,

        List<ApplierProfileCVSkillResponse> skills,

        String summary
) {
}
