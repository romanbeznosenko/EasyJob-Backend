package com.easyjob.easyjobapi.core.user.models;

import com.easyjob.easyjobapi.utils.enums.UserTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private UserId userId;
    private String email;
    private String name;
    private String surname;
    private Boolean blocked;
    private Integer points;
    private UserTypeEnum userType;

    private Boolean isArchived;
    private Instant archivedAt;

    private Instant createdAt;
    private Instant updatedAt;
}





