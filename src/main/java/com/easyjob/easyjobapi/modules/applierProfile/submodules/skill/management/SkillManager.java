package com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.management;

import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.models.SkillDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SkillManager {
    private final SkillRepository skillRepository;

    public SkillDAO saveToDatabase(SkillDAO skill) {
        return skillRepository.save(skill);
    }

    public Page<SkillDAO> findByApplierProfile_Id(UUID applierProfileId, Pageable pageable) {
        return skillRepository.findByApplierProfile_Id(applierProfileId, pageable);
    }

    public Optional<SkillDAO> findBySkillId(UUID skillId) {
        return skillRepository.findById(skillId);
    }
}