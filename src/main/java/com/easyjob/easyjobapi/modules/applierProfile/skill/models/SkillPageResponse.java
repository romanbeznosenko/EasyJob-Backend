package com.easyjob.easyjobapi.modules.applierProfile.skill.models;

import lombok.Builder;

import java.util.List;

@Builder
public record SkillPageResponse(
        long count,
        List<SkillResponse> data
) {
}
