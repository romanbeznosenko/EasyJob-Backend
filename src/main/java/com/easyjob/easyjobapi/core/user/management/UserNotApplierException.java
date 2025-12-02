package com.easyjob.easyjobapi.core.user.management;

public class UserNotApplierException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Logged user is not Applier";
    public UserNotApplierException() {
        super(DEFAULT_MESSAGE);
    }
}
