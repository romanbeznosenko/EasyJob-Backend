package com.easyjob.easyjobapi.core.passwordReset.models;

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
public class PasswordResetToken {
    private PasswordResetTokenId passwordResetTokenId;
    private String token;
    private Instant expiryDate;
    private Instant createdAt;
    private Instant updatedAt;
    private AuthAccount authAccount;
}





