package com.easyjob.easyjobapi.core.user.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.easyjob.easyjobapi.core.user.management.UserManager;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.core.user.models.UserEditRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserEditService {
    private final HttpServletRequest request;
    private final UserManager userManager;

    public void editUserDetails(UserEditRequest userEditRequest) {
        log.info("Editing user details");
        UserDAO userDAO = (UserDAO) request.getAttribute("user");

        userDAO.setName(userEditRequest.name());
        userDAO.setSurname(userEditRequest.surname());

        userManager.saveUserToDatabase(userDAO);
        log.info("Successfully edited user details");
    }
}
