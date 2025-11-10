package com.easyjob.easyjobapi.core.passwordReset.management;

public class PasswordResetTokenAlreadyGeneratedException extends Exception {
    public PasswordResetTokenAlreadyGeneratedException(String errorMessage) {
        super(errorMessage);
    }
}