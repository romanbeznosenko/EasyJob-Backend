package com.easyjob.easyjobapi.modules.applierProfile.management;

public class ApplierProfileNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Applier profile not found in the system.";
    public ApplierProfileNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
