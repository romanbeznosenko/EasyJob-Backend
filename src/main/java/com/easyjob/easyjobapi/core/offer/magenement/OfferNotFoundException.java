package com.easyjob.easyjobapi.core.offer.magenement;

public class OfferNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Offer not found";
    public OfferNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
