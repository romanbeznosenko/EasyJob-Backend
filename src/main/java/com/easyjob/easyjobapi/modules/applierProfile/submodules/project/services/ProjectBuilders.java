package com.easyjob.easyjobapi.modules.applierProfile.submodules.project.services;

import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.Project;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.ProjectId;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.ProjectRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectBuilders {
    public static Project buildFromRequest(ProjectRequest projectRequest, ApplierProfile applierProfile) {
        return Project.builder()
                .projectId(ProjectId.of(null))
                .applierProfile(applierProfile)
                .name(projectRequest.name())
                .description(projectRequest.description())
                .technologies(projectRequest.technologies())
                .link(projectRequest.link())
                .isArchived(false)
                .build();
    }
}
