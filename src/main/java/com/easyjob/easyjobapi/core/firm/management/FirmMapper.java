package com.easyjob.easyjobapi.core.firm.management;

import com.easyjob.easyjobapi.core.firm.models.Firm;
import com.easyjob.easyjobapi.core.firm.models.FirmDAO;
import com.easyjob.easyjobapi.core.firm.models.FirmId;
import com.easyjob.easyjobapi.core.user.management.UserMapper;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import org.mapstruct.*;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {UserMapper.class})
public interface FirmMapper {
    @Mapping(target = "id", expression = "java(toMap.getFirmId().getId())")
    FirmDAO mapToEntity(Firm toMap,
                        @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "firmId", source = "toMap", qualifiedByName = "longToObject")
    Firm mapToDomain(FirmDAO toMap,
                     @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Named("longToObject")
    default FirmId fromLongToObject(FirmDAO firmDAO) {
        return FirmId.of(firmDAO.getId());
    }
}