package com.easyjob.easyjobapi.utils;

import com.easyjob.easyjobapi.core.user.models.UserDAO;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Value
@Builder
public class CustomUserDetails implements UserDetails {
    //Default fields
    Collection<? extends GrantedAuthority> authorities;
    String email;
    String displayName;
    String password;
    String username;
    boolean enabled;
    boolean accountNonExpired;
    boolean accountNonLocked;
    boolean credentialsNonExpired;

    @ToString.Exclude
    UserDAO user;
}
