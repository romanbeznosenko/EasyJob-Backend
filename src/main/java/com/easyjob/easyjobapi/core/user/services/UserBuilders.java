package com.easyjob.easyjobapi.core.user.services;

import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.core.user.models.UserResponse;
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

    public static UserResponse buildUserResponseFromUser(UserDAO user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .surname(user.getSurname())
                .userType(user.getUserType())
                .blocked(user.getBlocked())
                .build();
    }
}
