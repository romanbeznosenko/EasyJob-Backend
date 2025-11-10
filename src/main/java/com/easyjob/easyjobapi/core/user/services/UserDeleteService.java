package com.easyjob.easyjobapi.core.user.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.easyjob.easyjobapi.core.authAccount.management.AuthAccountManager;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccountDAO;
import com.easyjob.easyjobapi.core.login.management.IncorrectPasswordException;
import com.easyjob.easyjobapi.core.user.management.UserManager;
import com.easyjob.easyjobapi.core.user.management.UserNotFoundException;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.core.user.models.UserDeleteRequest;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDeleteService {
    private final HttpServletRequest request;
    private final UserManager userManager;
    private final AuthAccountManager authAccountManager;
    private final PasswordEncoder passwordEncoder;

    public void deleteUser(UserDeleteRequest userDeleteRequest) throws UserNotFoundException {
        log.info("Deleting user");
        UserDAO userDAO = (UserDAO) request.getAttribute("user");

        AuthAccountDAO authAccountDAO = authAccountManager.findByUserId(userDAO.getId())
                                                          .orElseThrow(UserNotFoundException::new);
        String trimmedPassword = userDeleteRequest.password()
                                .trim();
        if (!passwordEncoder.matches(trimmedPassword, authAccountDAO.getPassword())) {
            throw new IncorrectPasswordException();
        }

        authAccountManager.deleteAuthAccount(authAccountDAO);

        String newEmail = Math.abs((userDAO.getId() + "_" + userDAO.getEmail()).hashCode()) + "@pksskorpion.local";
        userDAO.setEmail(newEmail);
        userDAO.setIsArchived(true);
        userDAO.setArchivedAt(Instant.now());
        userManager.saveUserToDatabase(userDAO);
        log.info("Successfully deleted user {}", userDAO.getId());
    }
}
