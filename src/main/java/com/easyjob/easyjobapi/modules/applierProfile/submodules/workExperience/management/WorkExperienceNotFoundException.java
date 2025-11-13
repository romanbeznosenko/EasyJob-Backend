package com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.management;

public class WorkExperienceNotFoundException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Work Experience Not Found";
    public WorkExperienceNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
