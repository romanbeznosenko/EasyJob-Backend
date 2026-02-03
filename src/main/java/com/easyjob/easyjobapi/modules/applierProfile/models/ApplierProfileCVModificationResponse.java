package com.easyjob.easyjobapi.modules.applierProfile.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ApplierProfileCVModificationResponse {
    @JsonProperty("personal_information")
    private PersonalInformation personalInformation;

    private String summary;
    private List<Education> education;

    @JsonProperty("work_experience")
    private List<WorkExperience> workExperience;

    private List<Project> projects;
    private List<Skill> skills;
    private List<Certification> certifications;
    private List<Language> languages;

    @Data
    public static class PersonalInformation {
        @JsonProperty("full_name")
        private String fullName;
        private String email;
        private String phone;
        private String location;
        private String linkedin;
    }

    @Data
    public static class Education {
        private String degree;
        private String university;
        private String major;
        @JsonProperty("start_date")
        private String startDate;
        @JsonProperty("end_date")
        private String endDate;
        private String gpa;
    }

    @Data
    public static class WorkExperience {
        private String title;
        @JsonProperty("company_name")
        private String companyName;
        @JsonProperty("start_date")
        private String startDate;
        @JsonProperty("end_date")
        private String endDate;
        private String location;
        private List<String> responsibilities;
    }

    @Data
    public static class Project {
        private String name;
        private String description;
        private List<String> technologies;
        private String link;
    }

    @Data
    public static class Skill {
        private String name;
        private String level;
        private String category;
    }

    @Data
    public static class Certification {
        private String name;
        private String issuer;
        private String date;
        private String url;
    }

    @Data
    public static class Language {
        private String name;
        private String proficiency;
    }
}