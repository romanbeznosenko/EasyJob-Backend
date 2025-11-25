package com.easyjob.easyjobapi.core.user.services;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.easyjob.easyjobapi.core.user.models.User;
import com.easyjob.easyjobapi.core.user.models.UserId;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserBuilders {

    public static User buildUserFromEmail(String email) {
        return User.builder()
                   .userId(UserId.of(null))
                   .email(email.strip()
                               .toLowerCase())
                   .isArchived(false)
                   .blocked(false)
                   .build();
    }
}
