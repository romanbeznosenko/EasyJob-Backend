package com.easyjob.easyjobapi.core.accountActivation.services;//package com.easyjob.easyjobapi.core.accountActivation.services;

import com.mailgun.exception.MailGunException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.easyjob.easyjobapi.core.accountActivation.management.AccountAlreadyActivatedException;
import com.easyjob.easyjobapi.core.accountActivation.management.ActivateAccountTokenAlreadyGeneratedException;
import com.easyjob.easyjobapi.core.accountActivation.management.VerificationCodeManager;
import com.easyjob.easyjobapi.core.accountActivation.management.VerificationCodeMapper;
import com.easyjob.easyjobapi.core.accountActivation.models.VerificationCode;
import com.easyjob.easyjobapi.core.accountActivation.models.VerificationCodeDAO;
import com.easyjob.easyjobapi.core.accountActivation.models.VerificationCodeResendRequest;
import com.easyjob.easyjobapi.core.authAccount.management.AuthAccountManager;
import com.easyjob.easyjobapi.core.authAccount.management.AuthAccountMapper;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccount;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccountDAO;
import com.easyjob.easyjobapi.core.user.management.UserNotFoundException;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import com.easyjob.easyjobapi.utils.mailer.ActivationEmailService;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class VerificationCodeResendService {
    private final AuthAccountManager authAccountManager;
    private final AuthAccountMapper authAccountMapper;
    private final VerificationCodeManager verificationCodeManager;
    private final VerificationCodeMapper verificationCodeMapper;
    private final VerificationCodeCreateService verificationCodeCreateService;
    private final ActivationEmailService activationEmailService;

    public void resendVerificationCode(VerificationCodeResendRequest request) throws UserNotFoundException, AccountAlreadyActivatedException, ActivateAccountTokenAlreadyGeneratedException {
        log.info("Resending account activation code to user: {}", request.email());

        String trimmedEmail = request.email()
                                     .strip();

        AuthAccountDAO authAccountDAO = authAccountManager.findByEmail(trimmedEmail)
                                                          .orElseThrow(
                                                                  () -> new UserNotFoundException("User not found!"));
        AuthAccount authAccount = authAccountMapper.mapToDomain(authAccountDAO, new CycleAvoidingMappingContext());
        if (Boolean.TRUE.equals(authAccount.getIsActivated()))
            throw new AccountAlreadyActivatedException("User is already activated!");

        VerificationCodeDAO verificationCodeDAO = verificationCodeManager.findVerificationCodeByUser(authAccountDAO)
                                                                         .orElse(null);
        VerificationCode verificationCode = verificationCodeMapper.mapToDomain(verificationCodeDAO,
                                                                               new CycleAvoidingMappingContext());
        long duration = Duration.between(LocalDateTime.now(), verificationCode.getCreatedAt())
                                .toMinutes();
        if (duration < 3)
            throw new ActivateAccountTokenAlreadyGeneratedException(
                    "Account activation token has been generated less than 3 minutes ago! Try again in " + (3 - duration) + " minutes");
        if (verificationCodeDAO != null) {
            verificationCodeManager.deleteVerificationCode(verificationCodeDAO);
        }

        verificationCodeDAO = verificationCodeCreateService.generateVerificationCode(authAccount);

        try {
            activationEmailService.sendActivationEmail(authAccountDAO, verificationCodeDAO.getCode());
        } catch (MailGunException e) {
            verificationCodeManager.deleteVerificationCode(verificationCodeDAO);
            throw e;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Resent account activation code to user: {}", request.email());
    }
}

