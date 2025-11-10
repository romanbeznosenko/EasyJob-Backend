package com.easyjob.easyjobapi.core.accountActivation.services;

import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileManager;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.easyjob.easyjobapi.core.accountActivation.management.*;
import com.easyjob.easyjobapi.core.accountActivation.models.AccountActivationRequest;
import com.easyjob.easyjobapi.core.accountActivation.models.VerificationCode;
import com.easyjob.easyjobapi.core.accountActivation.models.VerificationCodeDAO;
import com.easyjob.easyjobapi.core.accountActivation.models.VerificationCodeResendRequest;
import com.easyjob.easyjobapi.core.authAccount.management.AuthAccountManager;
import com.easyjob.easyjobapi.core.authAccount.management.AuthAccountMapper;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccount;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccountDAO;
import com.easyjob.easyjobapi.core.user.management.UserManager;
import com.easyjob.easyjobapi.core.user.management.UserNotFoundException;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountActivationService {
    private final VerificationCodeManager verificationCodeManager;
    private final VerificationCodeMapper verificationCodeMapper;
    private final VerificationCodeResendService verificationCodeResendService;
    private final AuthAccountMapper authAccountMapper;
    private final AuthAccountManager authAccountManager;
    private final UserManager userManager;
    private final ApplierProfileManager applierProfileManager;

    public void activateUser(AccountActivationRequest request) throws UserNotFoundException, AccountAlreadyActivatedException, AccountActivationTokenNotFoundException, ActivateAccountTokenExpiredException, ActivateAccountTokenAlreadyGeneratedException {
        log.info("Activating user");
        VerificationCodeDAO verificationCodeDAO = verificationCodeManager.findVerificationCodeByCode(request.code())
                                                                         .orElseThrow(
                                                                                 () -> new AccountActivationTokenNotFoundException(
                                                                                         "Account activation token not found!"));
        VerificationCode verificationCode = verificationCodeMapper.mapToDomain(verificationCodeDAO,
                                                                               new CycleAvoidingMappingContext());
        if (Instant.now()
                   .isAfter(verificationCode.getVerificationCodeExpireAt())) {
            verificationCodeResendService.resendVerificationCode(VerificationCodeResendRequest.builder()
                                                                                              .email(verificationCode.getUser()
                                                                                                                     .getEmail())
                                                                                              .build());
            throw new ActivateAccountTokenExpiredException("Account activation token has expired!");
        }

        AuthAccount authAccount = verificationCode.getUser();

        authAccount.setIsActivated(true);

        verificationCodeManager.deleteVerificationCode(verificationCodeDAO);
        log.info("Activated user");

        log.info("Creating user account");
        UserDAO userDAO = userManager.findUserById(authAccount.getUserId())
                .orElseThrow(UserNotFoundException::new);
        userDAO.setIsArchived(false);
        userManager.saveUserToDatabase(userDAO);

        ApplierProfileDAO applierProfileDAO = applierProfileManager.findByUser(userDAO.getId())
                        .orElseThrow(ApplierProfileNotFoundException::new);
        applierProfileDAO.setIsArchived(false);
        applierProfileManager.saveToDatabase(applierProfileDAO);

        authAccount.setUserId(userDAO.getId());
        AuthAccountDAO authAccountDAO = authAccountMapper.mapToEntity(authAccount, new CycleAvoidingMappingContext());
        authAccountManager.saveAuthAccountToDatabase(authAccountDAO);

        log.info("Creating default organization");
    }
}

