package com.easyjob.easyjobapi.core.offerApplicationEvaluation.models;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor(staticName = "of")
public class OfferApplicationEvaluationId {
    UUID id;
}
