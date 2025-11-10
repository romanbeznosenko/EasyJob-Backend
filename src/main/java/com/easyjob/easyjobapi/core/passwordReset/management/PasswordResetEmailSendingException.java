package com.easyjob.easyjobapi.core.passwordReset.management;

public class PasswordResetEmailSendingException extends Exception {
    public PasswordResetEmailSendingException(String errorMessage) {
        super(errorMessage);
    }
}