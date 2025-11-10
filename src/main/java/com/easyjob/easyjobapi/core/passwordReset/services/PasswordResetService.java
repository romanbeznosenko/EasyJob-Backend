package com.easyjob.easyjobapi.core.passwordReset.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.easyjob.easyjobapi.core.authAccount.management.AuthAccountManager;
import com.easyjob.easyjobapi.core.authAccount.management.AuthAccountMapper;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccount;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccountDAO;
import com.easyjob.easyjobapi.core.passwordReset.management.*;
import com.easyjob.easyjobapi.core.passwordReset.models.PasswordResetRequest;
import com.easyjob.easyjobapi.core.passwordReset.models.PasswordResetToken;
import com.easyjob.easyjobapi.core.passwordReset.models.PasswordResetTokenDAO;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class PasswordResetService {
    private final PasswordResetTokenMapper passwordResetTokenMapper;
    private final PasswordResetTokenManager passwordResetTokenManager;
    private final AuthAccountManager authAccountManager;
    private final AuthAccountMapper authAccountMapper;
    private final PasswordEncoder passwordEncoder;

    public void resetPassword(PasswordResetRequest request) throws PasswordResetTokenNotFoundException, PasswordResetTokenExpiredException, PasswordsAreNotMatchingException {
        log.info("Resetting user password");
        PasswordResetTokenDAO passwordResetTokenDAO = passwordResetTokenManager.findPasswordResetTokenByToken(
                                                                                       request.code())
                                                                               .orElseThrow(
                                                                                       () -> new PasswordResetTokenNotFoundException(
                                                                                               "Password reset token not found!"));
        PasswordResetToken passwordResetToken = passwordResetTokenMapper.mapToDomain(passwordResetTokenDAO,
                                                                                     new CycleAvoidingMappingContext());
        if (Instant.now()
                   .isAfter(passwordResetToken.getExpiryDate())) {
            throw new PasswordResetTokenExpiredException("Password reset token has expired!");
        }
        if (!request.newPassword()
                    .equals(request.confirmPassword()))
            throw new PasswordsAreNotMatchingException("New passwords are not the same!");
        AuthAccount authAccount = passwordResetToken.getAuthAccount();
        authAccount.setPassword(passwordEncoder.encode(request.newPassword()));
        AuthAccountDAO authAccountDAO = authAccountMapper.mapToEntity(authAccount, new CycleAvoidingMappingContext());
        authAccountManager.saveAuthAccountToDatabase(authAccountDAO);
        passwordResetTokenManager.deletePasswordResetToken(passwordResetTokenDAO);
        log.info("Reset password of user: {}", authAccount.getEmail());
    }
}

