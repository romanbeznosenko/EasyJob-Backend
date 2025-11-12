package com.easyjob.easyjobapi.modules.applierProfile.education.models;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record EducationResponse(
        UUID educationId,
        UUID applierProfileId,
        String degree,
        String university,
        LocalDate startDate,
        LocalDate endDate,
        String major,
        Double gpa
) {
}
