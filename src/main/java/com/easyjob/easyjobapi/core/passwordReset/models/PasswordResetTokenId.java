package com.easyjob.easyjobapi.core.passwordReset.models;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor(staticName = "of")
public class PasswordResetTokenId {
    UUID id;
}
