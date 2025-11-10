package com.easyjob.easyjobapi.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.easyjob.easyjobapi.core.user.management.UserDeletedException;
import com.easyjob.easyjobapi.core.user.management.UserManager;
import com.easyjob.easyjobapi.core.user.management.UserNotFoundException;
import com.easyjob.easyjobapi.core.user.models.UserDAO;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserInterceptor implements HandlerInterceptor {
    private final UserManager userManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws UserNotFoundException {

        log.info("UserInterceptor, handle user");
        UUID userId = (UUID) SecurityContextHolder.getContext()
                                                  .getAuthentication()
                                                  .getPrincipal();

        UserDAO user = userManager.findUserById(userId)
                                  .orElseThrow(UserNotFoundException::new);

        if (user.getIsArchived() == true) {
            throw new UserDeletedException();
        }

        request.setAttribute("user", user);
        log.info("UserInterceptor, set user successfully");
        return true;
    }
}
