package com.easyjob.easyjobapi.modules.applierProfile.workExperience.models;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor(staticName = "of")
public class WorkExperienceId {
    UUID id;
}
