package com.easyjob.easyjobapi.modules.applierProfile.management;

import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplierProfileManager {
    private final ApplierProfileRepository applierProfileRepository;

    public ApplierProfileDAO saveToDatabase(ApplierProfileDAO applierProfile) {
        return applierProfileRepository.save(applierProfile);
    }

    public Optional<ApplierProfileDAO> findByUser(UUID userId) {
        return applierProfileRepository.findByUser_Id(userId);
    }
}