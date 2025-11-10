package com.easyjob.easyjobapi.core.login.management;

public class IncorrectPasswordException extends RuntimeException {
    private final static String DEFAULT_MESSAGE = "Incorrect password.";
    public IncorrectPasswordException() {
        super(DEFAULT_MESSAGE);
    }
}
