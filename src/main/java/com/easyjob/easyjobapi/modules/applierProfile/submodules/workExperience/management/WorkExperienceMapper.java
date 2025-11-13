package com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.management;

import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileMapper;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.models.WorkExperience;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.models.WorkExperienceDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.models.WorkExperienceId;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.models.WorkExperienceResponse;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {ApplierProfileMapper.class})
public interface WorkExperienceMapper {
    @Mapping(target = "id", expression = "java(toMap.getWorkExperienceId().getId())")
    WorkExperienceDAO mapToEntity(WorkExperience toMap,
                                  @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "workExperienceId", source = "toMap", qualifiedByName = "longToObject")
    WorkExperience mapToDomain(WorkExperienceDAO toMap,
                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "workExperienceId", source = "id")
    @Mapping(target = "applierProfileId", source = "applierProfile.id")
    WorkExperienceResponse mapToResponse(WorkExperienceDAO workExperienceDAO,
                                         @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Named("longToObject")
    default WorkExperienceId fromLongToObject(WorkExperienceDAO workExperienceDAO) {
        return WorkExperienceId.of(workExperienceDAO.getId());
    }
}