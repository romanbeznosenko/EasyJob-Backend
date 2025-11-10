package com.easyjob.easyjobapi.core.passwordReset.management;

public class PasswordsAreNotMatchingException extends Exception {
    public PasswordsAreNotMatchingException(String errorMessage) {
        super(errorMessage);
    }
}