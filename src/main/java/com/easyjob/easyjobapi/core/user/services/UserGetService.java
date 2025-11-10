package com.easyjob.easyjobapi.core.user.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.easyjob.easyjobapi.core.user.management.UserMapper;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.core.user.models.UserResponse;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserGetService {
    private final HttpServletRequest request;
    private final UserMapper userMapper;

    public UserResponse getUserDetails() {
        log.info("Fetching user details");

        UserDAO user = (UserDAO) request.getAttribute("user");

        return userMapper.mapToResponseFromEntity(user, new CycleAvoidingMappingContext());
    }
}
