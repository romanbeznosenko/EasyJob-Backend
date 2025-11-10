package com.easyjob.easyjobapi.core.login.management;

public class AccountBlockedException extends Exception {
    public AccountBlockedException(String errorMessage) {
        super(errorMessage);
    }
}