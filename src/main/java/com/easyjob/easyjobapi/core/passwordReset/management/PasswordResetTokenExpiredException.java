package com.easyjob.easyjobapi.core.passwordReset.management;

public class PasswordResetTokenExpiredException extends Exception {
    public PasswordResetTokenExpiredException(String errorMessage) {
        super(errorMessage);
    }
}