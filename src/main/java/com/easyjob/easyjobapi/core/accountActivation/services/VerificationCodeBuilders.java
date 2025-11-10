package com.easyjob.easyjobapi.core.accountActivation.services;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.easyjob.easyjobapi.core.accountActivation.models.VerificationCode;
import com.easyjob.easyjobapi.core.accountActivation.models.VerificationCodeId;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccount;

import java.time.Instant;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class VerificationCodeBuilders {
    public static VerificationCode buildVerificationCode(String code, Instant verificationCodeExpireAt, AuthAccount user) {
        return VerificationCode.builder()
                               .verificationCodeId(VerificationCodeId.of(null))
                               .verificationCodeExpireAt(verificationCodeExpireAt)
                               .code(code)
                               .user(user)
                               .build();
    }
}
