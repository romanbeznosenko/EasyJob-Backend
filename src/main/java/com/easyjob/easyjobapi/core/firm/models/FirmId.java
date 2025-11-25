package com.easyjob.easyjobapi.core.firm.models;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor(staticName = "of")
public class FirmId {
    UUID id;
}
