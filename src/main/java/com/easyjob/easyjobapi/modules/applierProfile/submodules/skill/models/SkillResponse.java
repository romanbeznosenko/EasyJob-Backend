package com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.models;

import lombok.Builder;

import java.util.UUID;

@Builder
public record SkillResponse(
        UUID skillId,
        UUID applierProfileId,
        String name,
        String level
) {
}
