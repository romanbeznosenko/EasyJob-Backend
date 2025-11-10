package com.easyjob.easyjobapi.utils;

import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.utils.enums.UserTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    public CustomUserDetails loadUserAndAuthority(UserDAO user, List<String> permission) {

        return CustomUserDetails.builder()
                                .user(user)
                                .email(user.getEmail())
                                .authorities(userAuthorities(permission, user))
                                .build();
    }

    private Collection<? extends GrantedAuthority> userAuthorities(List<String> permissions,
                                                                   UserDAO user) {

        HashSet<GrantedAuthority> authorities = new HashSet<>();

        if (user.getUserType().equals(UserTypeEnum.APPLIER)){
            authorities.add(new SimpleGrantedAuthority("ROLE_APPLIER"));
        }

        //Permissions
        if (permissions != null) {
            permissions.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission)));
        }


        return authorities;
    }


    //Default
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

}
