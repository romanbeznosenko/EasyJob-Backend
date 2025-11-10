package com.easyjob.easyjobapi.core.passwordReset.management;

import org.mapstruct.*;
import com.easyjob.easyjobapi.core.authAccount.management.AuthAccountMapper;
import com.easyjob.easyjobapi.core.passwordReset.models.PasswordResetToken;
import com.easyjob.easyjobapi.core.passwordReset.models.PasswordResetTokenDAO;
import com.easyjob.easyjobapi.core.passwordReset.models.PasswordResetTokenId;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {AuthAccountMapper.class})
public interface PasswordResetTokenMapper {
    @Mapping(target = "id", expression = "java(toMap.getPasswordResetTokenId().getId())")
    PasswordResetTokenDAO mapToEntity(PasswordResetToken toMap,
                                      @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "passwordResetTokenId", source = "toMap", qualifiedByName = "longToObject")
    PasswordResetToken mapToDomain(PasswordResetTokenDAO toMap,
                                   @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Named("longToObject")
    default PasswordResetTokenId fromLongToObject(PasswordResetTokenDAO passwordResetTokenDAO) {
        return PasswordResetTokenId.of(passwordResetTokenDAO.getId());
    }
}

