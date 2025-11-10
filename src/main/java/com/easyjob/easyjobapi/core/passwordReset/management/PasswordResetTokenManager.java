package com.easyjob.easyjobapi.core.passwordReset.management;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccountDAO;
import com.easyjob.easyjobapi.core.passwordReset.models.PasswordResetTokenDAO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenManager {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public PasswordResetTokenDAO savePasswordResetTokenToDatabase(PasswordResetTokenDAO passwordResetTokenDAO) {
        return passwordResetTokenRepository.save(passwordResetTokenDAO);
    }

    public Optional<PasswordResetTokenDAO> findPasswordResetTokenByToken(String token) {
        return passwordResetTokenRepository.findPasswordResetTokenDAOByToken(token);
    }

    public Optional<PasswordResetTokenDAO> findPasswordResetTokenByUser(AuthAccountDAO authAccountDAO) {
        return passwordResetTokenRepository.findPasswordResetTokenDAOByAuthAccount(authAccountDAO);
    }

    public Optional<PasswordResetTokenDAO> findPasswordResetTokenById(UUID id) {
        return passwordResetTokenRepository.findById(id);
    }

    public List<PasswordResetTokenDAO> findAllPasswordResetToken() {
        return (List<PasswordResetTokenDAO>) passwordResetTokenRepository.findAll();
    }

    public void deletePasswordResetToken(PasswordResetTokenDAO passwordResetTokenDAO) {
        passwordResetTokenRepository.delete(passwordResetTokenDAO);
    }
}
