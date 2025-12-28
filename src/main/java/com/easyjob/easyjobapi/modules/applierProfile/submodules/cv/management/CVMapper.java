package com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.management;

import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CV;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CVDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CVId;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface CVMapper {
    @Mapping(target = "id", expression = "java(toMap.getCvId().getId())")
    CVDAO mapToEntity(CV toMap,
                      @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "cvId", source = "toMap", qualifiedByName = "longToObject")
    CV mapToDomain(CVDAO toMap,
                   @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Named("longToObject")
    default CVId fromLongToObject(CVDAO cvDAO) {
        return CVId.of(cvDAO.getId());
    }
}