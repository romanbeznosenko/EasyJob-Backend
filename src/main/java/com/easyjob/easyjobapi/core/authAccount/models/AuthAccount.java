package com.easyjob.easyjobapi.core.authAccount.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.easyjob.easyjobapi.utils.enums.AuthTypeEnum;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthAccount {

    private AuthAccountId authAccountId;
    private String email;
    private String providerId;
    private AuthTypeEnum authType;
    private UUID userId;
    private String password;
    private Boolean isActivated;
    private Instant createdAt;
    private Instant updatedAt;
}





