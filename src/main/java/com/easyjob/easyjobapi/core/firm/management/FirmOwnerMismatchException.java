package com.easyjob.easyjobapi.core.firm.management;

public class FirmOwnerMismatchException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Logged user does not have permission for this operation.";
    public FirmOwnerMismatchException() {
        super(DEFAULT_MESSAGE);
    }
}
