package com.easyjob.easyjobapi.core.user.management;

public class UserAlreadyHasFirmException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "User Already Has Firm";
    public UserAlreadyHasFirmException() {
        super(DEFAULT_MESSAGE);
    }
}
