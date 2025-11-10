package com.easyjob.easyjobapi.modules.applierProfile.management;

import com.easyjob.easyjobapi.core.user.management.UserMapper;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileId;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {UserMapper.class})
public interface ApplierProfileMapper {
    @Mapping(target = "id", expression = "java(toMap.getApplierProfileId().getId())")
    ApplierProfileDAO mapToEntity(ApplierProfile toMap,
                                  @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "applierProfileId", source = "toMap", qualifiedByName = "longToObject")
    ApplierProfile mapToDomain(ApplierProfileDAO toMap,
                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Named("longToObject")
    default ApplierProfileId fromLongToObject(ApplierProfileDAO applierProfileDAO) {
        return ApplierProfileId.of(applierProfileDAO.getId());
    }
}