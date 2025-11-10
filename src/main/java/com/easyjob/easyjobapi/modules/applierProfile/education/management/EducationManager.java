package com.easyjob.easyjobapi.modules.applierProfile.education.management;

import com.easyjob.easyjobapi.modules.applierProfile.education.models.EducationDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EducationManager {
    private final EducationRepository educationRepository;

    public EducationDAO saveToDatabase(EducationDAO education) {
        return educationRepository.save(education);
    }
}