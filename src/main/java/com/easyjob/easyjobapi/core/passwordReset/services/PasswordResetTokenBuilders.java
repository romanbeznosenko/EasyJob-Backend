package com.easyjob.easyjobapi.core.passwordReset.services;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccount;
import com.easyjob.easyjobapi.core.passwordReset.models.PasswordResetToken;
import com.easyjob.easyjobapi.core.passwordReset.models.PasswordResetTokenId;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordResetTokenBuilders {
    public static PasswordResetToken buildPasswordResetToken(AuthAccount authAccount, String token) {
        return PasswordResetToken.builder()
                                 .passwordResetTokenId(PasswordResetTokenId.of(null))
                                 .token(token)
                                 .expiryDate(Instant.now()
                                                    .plus(60, ChronoUnit.MINUTES))
                                 .authAccount(authAccount)
                                 .build();
    }
}
