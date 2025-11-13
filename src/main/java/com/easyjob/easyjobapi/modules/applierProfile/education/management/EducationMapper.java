package com.easyjob.easyjobapi.modules.applierProfile.education.management;

import com.easyjob.easyjobapi.modules.applierProfile.education.models.Education;
import com.easyjob.easyjobapi.modules.applierProfile.education.models.EducationDAO;
import com.easyjob.easyjobapi.modules.applierProfile.education.models.EducationId;
import com.easyjob.easyjobapi.modules.applierProfile.education.models.EducationResponse;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileMapper;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {ApplierProfileMapper.class})
public interface EducationMapper {
    @Mapping(target = "id", expression = "java(toMap.getEducationId().getId())")
    EducationDAO mapToEntity(Education toMap,
                             @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "educationId", source = "toMap", qualifiedByName = "longToObject")
    Education mapToDomain(EducationDAO toMap,
                          @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "educationId", source = "id")
    @Mapping(target = "applierProfileId", source = "applierProfile.id")
    EducationResponse mapToResponse(EducationDAO educationDAO,
                                    @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Named("longToObject")
    default EducationId fromLongToObject(EducationDAO educationDAO) {
        return EducationId.of(educationDAO.getId());
    }
}