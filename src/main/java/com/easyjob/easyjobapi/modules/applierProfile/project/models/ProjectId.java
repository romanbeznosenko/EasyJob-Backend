package com.easyjob.easyjobapi.modules.applierProfile.project.models;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor(staticName = "of")
public class ProjectId {
    UUID id;
}
