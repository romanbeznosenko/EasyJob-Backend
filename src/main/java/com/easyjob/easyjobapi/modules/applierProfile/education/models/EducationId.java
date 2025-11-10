package com.easyjob.easyjobapi.modules.applierProfile.education.models;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor(staticName = "of")
public class EducationId {
    UUID id;
}
