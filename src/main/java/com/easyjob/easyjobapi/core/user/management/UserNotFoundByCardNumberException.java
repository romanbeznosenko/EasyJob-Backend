package com.easyjob.easyjobapi.core.user.management;

public class UserNotFoundByCardNumberException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "User not found by card number";

    public UserNotFoundByCardNumberException() {
        super(DEFAULT_MESSAGE);
    }
}
