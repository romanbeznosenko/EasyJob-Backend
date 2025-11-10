package com.easyjob.easyjobapi.core.passwordReset.management;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccountDAO;
import com.easyjob.easyjobapi.core.passwordReset.models.PasswordResetTokenDAO;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenDAO, UUID> {
    Optional<PasswordResetTokenDAO> findPasswordResetTokenDAOByToken(String token);

    Optional<PasswordResetTokenDAO> findPasswordResetTokenDAOByAuthAccount(AuthAccountDAO authAccountDAO);
}

