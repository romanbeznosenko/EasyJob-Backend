package com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor(staticName = "of")
public class CVId {
    UUID id;
}
