package com.easyjob.easyjobapi.modules.applierProfile.skill.models;

import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Skill {
    private SkillId skillId;
    private ApplierProfile applierProfile;
    private String name;
    private String level;

    private Instant createdAt;
    private Instant updatedAt;

    private Boolean isArchived;
    private Instant archivedAt;
}
