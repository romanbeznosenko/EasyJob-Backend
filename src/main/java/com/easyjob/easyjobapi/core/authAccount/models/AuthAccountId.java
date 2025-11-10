package com.easyjob.easyjobapi.core.authAccount.models;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor(staticName = "of")
public class AuthAccountId {

    UUID id;
}
