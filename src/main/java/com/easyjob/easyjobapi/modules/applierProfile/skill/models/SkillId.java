package com.easyjob.easyjobapi.modules.applierProfile.skill.models;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor(staticName = "of")
public class SkillId {
    UUID id;
}
