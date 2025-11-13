package com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.management;

import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.models.WorkExperienceDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkExperienceManager {
    private final WorkExperienceRepository workExperienceRepository;

    public WorkExperienceDAO saveToDatabase(WorkExperienceDAO workExperience) {
        return workExperienceRepository.save(workExperience);
    }

    public Page<WorkExperienceDAO> findByApplierProfile(UUID applierProfileId, Pageable pageable) {
        return workExperienceRepository.findByApplierProfile_Id(applierProfileId, pageable);
    }

    public Optional<WorkExperienceDAO> findByWorkExperienceId(UUID workExperienceId) {
        return workExperienceRepository.findById(workExperienceId);
    }
}