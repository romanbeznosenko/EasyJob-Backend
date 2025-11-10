package com.easyjob.easyjobapi.core.accountActivation.management;

public class ActivateAccountTokenAlreadyGeneratedException extends Exception {
    public ActivateAccountTokenAlreadyGeneratedException(String errorMessage) {
        super(errorMessage);
    }
}