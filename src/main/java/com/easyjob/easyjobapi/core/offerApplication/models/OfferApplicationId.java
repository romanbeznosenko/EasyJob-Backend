package com.easyjob.easyjobapi.core.offerApplication.models;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor(staticName = "of")
public class OfferApplicationId {
    UUID id;
}
