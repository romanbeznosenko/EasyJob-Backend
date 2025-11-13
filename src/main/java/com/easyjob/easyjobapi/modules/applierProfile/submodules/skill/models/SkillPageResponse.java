package com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.models;

import lombok.Builder;

import java.util.List;

@Builder
public record SkillPageResponse(
        long count,
        List<SkillResponse> data
) {
}
