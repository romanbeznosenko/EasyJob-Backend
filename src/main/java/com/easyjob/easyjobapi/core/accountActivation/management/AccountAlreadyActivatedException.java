package com.easyjob.easyjobapi.core.accountActivation.management;

public class AccountAlreadyActivatedException extends Exception {
    public AccountAlreadyActivatedException(String errorMessage) {
        super(errorMessage);
    }
}