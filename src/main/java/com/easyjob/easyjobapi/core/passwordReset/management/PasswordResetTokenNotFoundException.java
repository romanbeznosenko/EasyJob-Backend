package com.easyjob.easyjobapi.core.passwordReset.management;

public class PasswordResetTokenNotFoundException extends Exception {
    public PasswordResetTokenNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}