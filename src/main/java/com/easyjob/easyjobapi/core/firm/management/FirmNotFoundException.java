package com.easyjob.easyjobapi.core.firm.management;

public class FirmNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Firm Not Found";
    public FirmNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
