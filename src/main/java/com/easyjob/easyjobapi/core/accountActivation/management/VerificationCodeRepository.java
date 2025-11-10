package com.easyjob.easyjobapi.core.accountActivation.management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.easyjob.easyjobapi.core.accountActivation.models.VerificationCodeDAO;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccountDAO;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCodeDAO, UUID> {
    Optional<VerificationCodeDAO> findByCode(String code);

    Optional<VerificationCodeDAO> findByUser(AuthAccountDAO authAccountDAO);
}

