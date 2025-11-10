package com.easyjob.easyjobapi.core.user.management;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.easyjob.easyjobapi.core.user.models.UserDAO;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserManager {

    private final UserRepository userRepository;

    public UserDAO saveUserToDatabase(UserDAO userDAO) {
        return userRepository.save(userDAO);
    }

    public Optional<UserDAO> findUserById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<UserDAO> findUserByEmailAndArchivedFalse(String email) {
        return userRepository.findUserDAOByEmailAndIsArchivedFalse(email);
    }


}
