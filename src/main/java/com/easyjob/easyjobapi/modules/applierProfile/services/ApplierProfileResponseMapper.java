package com.easyjob.easyjobapi.modules.applierProfile.services;

import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileResponse;

import java.util.stream.Collectors;

public class ApplierProfileResponseMapper {
    public static String mapToString(ApplierProfileResponse response) {
        if (response == null) {
            return "null";
        }

        String educationStr = response.education().stream()
                .map(item -> String.format("[degree=%s, university=%s, startDate=%s, endDate=%s, major=%s, gpa=%s]",
                        item.degree(), item.university(), item.startDate(), item.endDate(), item.major(), item.gpa()))
                .collect(Collectors.joining(", "));

        String projectStr = response.project().stream()
                .map(item -> String.format("[name=%s, description=%s, technologies=%s, link=%s]",
                        item.name(), item.description(), item.technologies(), item.ling()))
                .collect(Collectors.joining(", "));

        String skillStr = response.skill().stream()
                .map(item -> String.format("[name=%s, level=%s]",
                        item.name(), item.level()))
                .collect(Collectors.joining(", "));

        String workExpStr = response.workExperience().stream()
                .map(item -> String.format("[title=%s, companyName=%s, startDate=%s, endDate=%s, responsibilities=%s, location=%s]",
                        item.title(), item.companyName(), item.startDate(), item.endDate() == null ? "To present" : item.endDate(), item.responsibilities(), item.location()))
                .collect(Collectors.joining(", "));

        return String.format("ApplierProfileResponse{user=%s, education=[%s], projects=[%s], skills=[%s], workExperience=[%s]}",
                response.user() != null ? response.user().name() + " " + response.user().surname() : "null",
                educationStr,
                projectStr,
                skillStr,
                workExpStr);
    }
}
