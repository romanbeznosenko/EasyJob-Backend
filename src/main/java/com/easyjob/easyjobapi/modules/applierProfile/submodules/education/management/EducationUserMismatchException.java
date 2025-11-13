package com.easyjob.easyjobapi.modules.applierProfile.submodules.education.management;

public class EducationUserMismatchException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Education user mismatch.";
    public EducationUserMismatchException() {
        super(DEFAULT_MESSAGE);
    }
}
