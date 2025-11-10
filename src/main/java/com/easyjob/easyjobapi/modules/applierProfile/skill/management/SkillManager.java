package com.easyjob.easyjobapi.modules.applierProfile.skill.management;

import com.easyjob.easyjobapi.modules.applierProfile.skill.models.SkillDAO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkillManager {
    private final SkillRepository skillRepository;

    public SkillDAO saveToDatabase(SkillDAO skill) {
        return skillRepository.save(skill);
    }
}