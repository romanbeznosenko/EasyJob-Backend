package com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.management;

public class CVNotFoundException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "CV bot found in system.";
    public CVNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
