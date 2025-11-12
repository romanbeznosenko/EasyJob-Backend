package com.easyjob.easyjobapi.modules.applierProfile.education.management;

import com.easyjob.easyjobapi.modules.applierProfile.education.models.EducationDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EducationManager {
    private final EducationRepository educationRepository;

    public EducationDAO saveToDatabase(EducationDAO education) {
        return educationRepository.save(education);
    }

    public Optional<EducationDAO> findByEducationId(UUID educationId) {
        return educationRepository.findById(educationId);
    }

    public Page<EducationDAO> findByApplierProfile_Id(UUID applierProfileId, Pageable pageable) {
        return educationRepository.findByApplierProfile_Id(applierProfileId, pageable);
    }
}