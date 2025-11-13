package com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.management;

public class WorkExperienceUserMismatchException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Work Experience User mismatch";
    public WorkExperienceUserMismatchException() {
        super(DEFAULT_MESSAGE);
    }
}
