package com.easyjob.easyjobapi.core.offer.models;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor(staticName = "of")
public class OfferId {
    UUID id;
}
