package com.easyjob.easyjobapi.core.accountActivation.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.easyjob.easyjobapi.core.accountActivation.management.VerificationCodeManager;
import com.easyjob.easyjobapi.core.accountActivation.management.VerificationCodeMapper;
import com.easyjob.easyjobapi.core.accountActivation.models.VerificationCode;
import com.easyjob.easyjobapi.core.accountActivation.models.VerificationCodeDAO;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccount;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class VerificationCodeCreateService {
    private final VerificationCodeManager verificationCodeManager;
    private final VerificationCodeMapper verificationCodeMapper;

    public VerificationCodeDAO generateVerificationCode(AuthAccount user) {
        log.info("Creating verification code for user: {}", user.getEmail());
        Random rand = new Random();
        String code = String.format("%04d", rand.nextInt(10000));
        Instant verificationCodeExpireAt = Instant.now()
                                                  .plus(24 * 60, ChronoUnit.MINUTES);
        VerificationCode verificationCode = VerificationCodeBuilders.buildVerificationCode(code,
                                                                                           verificationCodeExpireAt,
                                                                                           user);
        VerificationCodeDAO verificationCodeDAO = verificationCodeMapper.mapToEntity(verificationCode,
                                                                                     new CycleAvoidingMappingContext());
        log.info("Created verification code for user: {}", user.getEmail());
        return verificationCodeManager.saveVerificationCodeToDatabase(verificationCodeDAO);
    }
}
