package com.easyjob.easyjobapi.core.register.services;

import com.easyjob.easyjobapi.core.accountActivation.services.VerificationCodeSendService;
import com.easyjob.easyjobapi.core.user.management.UserManager;
import com.easyjob.easyjobapi.core.user.management.UserMapper;
import com.easyjob.easyjobapi.core.user.models.User;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.core.user.services.UserBuilders;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileManager;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileMapper;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import com.easyjob.easyjobapi.modules.applierProfile.services.ApplierProfileBuilders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//import com.easyjob.easyjobapi.core.accountActivation.services.VerificationCodeSendService;
import com.easyjob.easyjobapi.core.authAccount.management.AuthAccountManager;
import com.easyjob.easyjobapi.core.authAccount.management.AuthAccountMapper;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccount;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccountDAO;
import com.easyjob.easyjobapi.core.authAccount.services.AuthAccountBuilders;
import com.easyjob.easyjobapi.core.register.management.UserAlreadyExistException;
import com.easyjob.easyjobapi.core.register.models.RegisterRequest;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import com.easyjob.easyjobapi.utils.enums.AuthTypeEnum;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegisterService {
    private final PasswordEncoder passwordEncoder;
    private final AuthAccountManager authAccountManager;
    private final AuthAccountMapper authAccountMapper;
    private final VerificationCodeSendService verificationCodeSendService;
    private final UserMapper userMapper;
    private final UserManager userManager;
    private final ApplierProfileManager applierProfileManager;
    private final ApplierProfileMapper applierProfileMapper;

    public void registerUser(RegisterRequest request) throws UserAlreadyExistException, IOException {
        log.info("Creating new app user: {}", request.email());
        String trimmedEmail = request.email().strip().toLowerCase();
        String trimmedPassword = request.password().strip();

        AuthAccountDAO authAccountDAO = authAccountManager.findByEmail(trimmedEmail).orElse(null);
        if (authAccountDAO != null && authAccountDAO.getIsActivated())
            throw new UserAlreadyExistException("User already exist!");

        if (authAccountDAO == null) {
            AuthAccount authAccount = AuthAccountBuilders.buildAuthAccount(
                    trimmedEmail,
                    passwordEncoder.encode(trimmedPassword),
                    AuthTypeEnum.EMAIL
            );
            authAccountDAO = authAccountMapper.mapToEntity(authAccount, new CycleAvoidingMappingContext());
            authAccountDAO.setIsActivated(false);
            authAccountDAO = authAccountManager.saveAuthAccountToDatabase(authAccountDAO);

            // Create and save User
            User user = UserBuilders.buildUserFromEmail(request.email());
            UserDAO userDAO = userMapper.mapToEntity(user, new CycleAvoidingMappingContext());
            userDAO.setUserType(request.userType());
            userDAO.setIsArchived(true);
            userDAO = userManager.saveUserToDatabase(userDAO);

            // Create ApplierProfile using the MANAGED UserDAO entity directly
            ApplierProfileDAO applierProfileDAO = ApplierProfileDAO.builder()
                    .user(userDAO)  // ← Use the managed entity directly
                    .isArchived(true)
                    .build();
            applierProfileManager.saveToDatabase(applierProfileDAO);

            // Update authAccount with userId
            authAccountDAO.setUserId(userDAO.getId());
            authAccountDAO = authAccountManager.saveAuthAccountToDatabase(authAccountDAO);
        }

        verificationCodeSendService.sendVerificationCode(authAccountDAO);
        log.info("Created new app user: {}", request.email());
    }
}

