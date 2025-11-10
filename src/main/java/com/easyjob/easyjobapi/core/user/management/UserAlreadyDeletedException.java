package com.easyjob.easyjobapi.core.user.management;

public class UserAlreadyDeletedException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "User already deleted";

    public UserAlreadyDeletedException() {
        super(DEFAULT_MESSAGE);
    }
}
