package com.easyjob.easyjobapi.modules.applierProfile.submodules.education.management;

public class EducationNotFoundException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Education not found";
    public EducationNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
