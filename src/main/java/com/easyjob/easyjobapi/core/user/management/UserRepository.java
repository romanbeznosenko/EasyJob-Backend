package com.easyjob.easyjobapi.core.user.management;

import org.springframework.data.jpa.repository.JpaRepository;
import com.easyjob.easyjobapi.core.user.models.UserDAO;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserDAO, UUID> {
    Optional<UserDAO> findUserDAOByEmailAndIsArchivedFalse(String email);
}

