package com.easyjob.easyjobapi.modules.applierProfile.project.management;

import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileMapper;
import com.easyjob.easyjobapi.modules.applierProfile.project.models.Project;
import com.easyjob.easyjobapi.modules.applierProfile.project.models.ProjectDAO;
import com.easyjob.easyjobapi.modules.applierProfile.project.models.ProjectId;
import com.easyjob.easyjobapi.modules.applierProfile.project.models.ProjectResponse;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {ApplierProfileMapper.class})
public interface ProjectMapper {
    @Mapping(target = "id", expression = "java(toMap.getProjectId().getId())")
    ProjectDAO mapToEntity(Project toMap,
                           @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "projectId", source = "toMap", qualifiedByName = "longToObject")
    Project mapToDomain(ProjectDAO toMap,
                        @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "projectId", source = "id")
    @Mapping(target = "applierProfileId", source = "applierProfile.id")
    ProjectResponse mapToResponse(ProjectDAO projectDAO,
                                  @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);


    @Named("longToObject")
    default ProjectId fromLongToObject(ProjectDAO projectDAO) {
        return ProjectId.of(projectDAO.getId());
    }
}