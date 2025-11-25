package com.easyjob.easyjobapi.core.firm.services;

import com.easyjob.easyjobapi.core.firm.models.*;
import com.easyjob.easyjobapi.core.user.management.UserMapper;
import com.easyjob.easyjobapi.core.user.models.User;
import com.easyjob.easyjobapi.files.storage.services.StorageService;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FirmBuilders {
    public static Firm buildFromRequest(FirmRequest firmRequest, User user) {
        return Firm.builder()
                .firmId(FirmId.of(null))
                .name(firmRequest.name())
                .user(user)
                .description(firmRequest.description())
                .location(firmRequest.location())
                .logo(null)
                .isArchived(false)
                .build();
    }

    public static FirmResponse buildResponse(FirmDAO firm, StorageService storageService, UserMapper userMapper) {
        return FirmResponse.builder()
                .firmId(firm.getId())
                .name(firm.getName())
                .owner(userMapper.mapToResponseFromEntity(firm.getUser(), new CycleAvoidingMappingContext()))
                .description(firm.getDescription())
                .location(firm.getLocation())
                .logo(storageService.createPresignedGetUrl(firm.getLogo()))
                .build();
    }
}
