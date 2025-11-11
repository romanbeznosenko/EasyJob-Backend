package com.easyjob.easyjobapi.modules.applierProfile.skill.services;

import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import com.easyjob.easyjobapi.modules.applierProfile.skill.models.Skill;
import com.easyjob.easyjobapi.modules.applierProfile.skill.models.SkillId;
import com.easyjob.easyjobapi.modules.applierProfile.skill.models.SkillRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SkillBuilders {
    public static Skill buildFromRequest(SkillRequest skillRequest, ApplierProfile applierProfile) {
        return Skill.builder()
                .skillId(SkillId.of(null))
                .applierProfile(applierProfile)
                .name(skillRequest.name())
                .level(skillRequest.level())
                .isArchived(false)
                .build();
    }
}
