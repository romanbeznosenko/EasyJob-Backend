package com.easyjob.easyjobapi.core.authAccount.management;


import org.mapstruct.*;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccount;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccountDAO;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccountId;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface AuthAccountMapper {

    @Mapping(target = "id", expression = "java(toMap.getAuthAccountId().getId())")
    AuthAccountDAO mapToEntity(AuthAccount toMap,
                               @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "authAccountId", source = "toMap", qualifiedByName = "longToObject")
    AuthAccount mapToDomain(AuthAccountDAO toMap,
                            @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Named("longToObject")
    default AuthAccountId fromLongToObject(AuthAccountDAO authAccountDAO) {
        return AuthAccountId.of(authAccountDAO.getId());
    }
}

