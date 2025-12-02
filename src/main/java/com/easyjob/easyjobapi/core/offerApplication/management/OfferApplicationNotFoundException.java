package com.easyjob.easyjobapi.core.offerApplication.management;

public class OfferApplicationNotFoundException extends RuntimeException
{
    private final static String DEFAULT_MESSAGE = "Offer Application Not Found";
    public OfferApplicationNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
