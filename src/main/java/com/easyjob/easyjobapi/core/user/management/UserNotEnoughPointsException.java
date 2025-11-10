package com.easyjob.easyjobapi.core.user.management;

public class UserNotEnoughPointsException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "User does not have enough points";

    public UserNotEnoughPointsException() {
        super(DEFAULT_MESSAGE);
    }
}
