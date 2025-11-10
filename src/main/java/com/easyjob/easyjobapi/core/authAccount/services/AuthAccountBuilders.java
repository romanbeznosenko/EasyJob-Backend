package com.easyjob.easyjobapi.core.authAccount.services;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccount;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccountId;
import com.easyjob.easyjobapi.utils.enums.AuthTypeEnum;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthAccountBuilders {

    public static AuthAccount buildAuthAccount(String email, String password, AuthTypeEnum authType) {
        return AuthAccount.builder()
                          .authAccountId(AuthAccountId.of(null))
                          .isActivated(false)
                          .email(email)
                          .password(password)
                          .authType(authType)
                          .build();
    }
}
