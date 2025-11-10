package com.easyjob.easyjobapi.core.user.management;

import org.mapstruct.*;
import com.easyjob.easyjobapi.core.user.models.User;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.core.user.models.UserId;
import com.easyjob.easyjobapi.core.user.models.UserResponse;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface UserMapper {

    @Mapping(target = "id", expression = "java(toMap.getUserId().getId())")
    UserDAO mapToEntity(User toMap,
                        @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "userId", source = "toMap", qualifiedByName = "longToObject")
    User mapToDomain(UserDAO toMap,
                     @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    UserResponse mapToResponseFromEntity(UserDAO toMap,
                                         @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Named("longToObject")
    default UserId fromLongToObject(UserDAO userDAO) {
        return UserId.of(userDAO.getId());
    }
}

