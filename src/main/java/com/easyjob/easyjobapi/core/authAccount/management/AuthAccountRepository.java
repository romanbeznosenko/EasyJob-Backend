package com.easyjob.easyjobapi.core.authAccount.management;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccountDAO;
import com.easyjob.easyjobapi.utils.enums.AuthTypeEnum;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthAccountRepository extends CrudRepository<AuthAccountDAO, UUID> {

    Optional<AuthAccountDAO> findByEmail(String email);

    Optional<AuthAccountDAO> findByUserId(UUID id);

    Optional<AuthAccountDAO> findByEmailAndAuthType(String email, AuthTypeEnum authType);
}

