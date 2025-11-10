package com.easyjob.easyjobapi.core.accountActivation.services;//package com.easyjob.easyjobapi.core.accountActivation.services;

import com.mailgun.exception.MailGunException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.easyjob.easyjobapi.core.accountActivation.management.VerificationCodeManager;
import com.easyjob.easyjobapi.core.accountActivation.models.VerificationCodeDAO;
import com.easyjob.easyjobapi.core.authAccount.management.AuthAccountManager;
import com.easyjob.easyjobapi.core.authAccount.management.AuthAccountMapper;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccountDAO;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import com.easyjob.easyjobapi.utils.mailer.ActivationEmailService;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class VerificationCodeSendService {
    private final VerificationCodeCreateService verificationCodeCreateService;
    private final VerificationCodeManager verificationCodeManager;
    private final ActivationEmailService activationEmailService;
    private final AuthAccountManager authAccountManager;
    private final AuthAccountMapper authAccountMapper;

    public void sendVerificationCode(AuthAccountDAO authAccountDAO) throws IOException {
        log.info("Sending account activation code to user: {}", authAccountDAO.getEmail());

        VerificationCodeDAO verificationCodeDAO = verificationCodeCreateService.generateVerificationCode(
                authAccountMapper.mapToDomain(authAccountDAO, new CycleAvoidingMappingContext()));
        try {
            activationEmailService.sendActivationEmail(authAccountDAO, verificationCodeDAO.getCode());
        } catch (MailGunException | IOException e) {
            verificationCodeManager.deleteVerificationCode(verificationCodeDAO);
            authAccountManager.deleteAuthAccount(authAccountDAO);
            throw e;
        }

        log.info("Sent account activation code to user: {}", authAccountDAO.getEmail());
    }
}

