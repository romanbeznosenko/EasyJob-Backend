package com.easyjob.easyjobapi.modules.applierProfile.services;

import com.easyjob.easyjobapi.core.user.models.User;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplierProfileBuilders {
    public static ApplierProfile buildFromUser(User user){
        return ApplierProfile.builder()
                .applierProfileId(ApplierProfileId.of(null))
                .user(user)
                .cv(null)
                .build();
    }
}
