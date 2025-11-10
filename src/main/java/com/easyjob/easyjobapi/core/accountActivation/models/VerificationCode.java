package com.easyjob.easyjobapi.core.accountActivation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccount;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCode {
    private VerificationCodeId verificationCodeId;
    private AuthAccount user;
    private Instant verificationCodeExpireAt;
    private String code;
    private Instant createdAt;
    private Instant updatedAt;
}





