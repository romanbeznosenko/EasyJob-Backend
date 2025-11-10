package com.easyjob.easyjobapi.core.user.management;

public class UserDeletedException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "User has been deleted";

    public UserDeletedException() {
        super(DEFAULT_MESSAGE);
    }
}
