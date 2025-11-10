package com.easyjob.easyjobapi.core.accountActivation.management;

import org.mapstruct.*;
import com.easyjob.easyjobapi.core.accountActivation.models.VerificationCode;
import com.easyjob.easyjobapi.core.accountActivation.models.VerificationCodeDAO;
import com.easyjob.easyjobapi.core.accountActivation.models.VerificationCodeId;
import com.easyjob.easyjobapi.core.authAccount.management.AuthAccountMapper;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true), uses = {AuthAccountMapper.class})
public interface VerificationCodeMapper {

    @Mapping(target = "id", expression = "java(toMap.getVerificationCodeId().getId())")
    VerificationCodeDAO mapToEntity(VerificationCode toMap,
                                    @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Mapping(target = "verificationCodeId", source = "toMap", qualifiedByName = "longToObject")
    VerificationCode mapToDomain(VerificationCodeDAO toMap,
                                 @Context CycleAvoidingMappingContext cycleAvoidingMappingContext);

    @Named("longToObject")
    default VerificationCodeId fromLongToObject(VerificationCodeDAO verificationCodeDAO) {
        return VerificationCodeId.of(verificationCodeDAO.getId());
    }
}

