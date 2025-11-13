package com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.services;

import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileManager;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileMapper;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.management.SkillManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.management.SkillMapper;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.models.Skill;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.models.SkillDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.models.SkillRequest;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class SkillCreateService {
    private final HttpServletRequest request;
    private final SkillManager skillManager;
    private final SkillMapper skillMapper;
    private final ApplierProfileManager applierProfileManager;
    private final ApplierProfileMapper applierProfileMapper;

    public void create(SkillRequest skillRequest) {
        log.info("Creating skill ...");

        UserDAO userDAO = (UserDAO) request.getAttribute("user");

        ApplierProfileDAO applierProfileDAO = applierProfileManager.findByUser(userDAO.getId())
                .orElseThrow(ApplierProfileNotFoundException::new);
        ApplierProfile applierProfile = applierProfileMapper.mapToDomain(applierProfileDAO, new CycleAvoidingMappingContext());

        Skill skill = SkillBuilders.buildFromRequest(skillRequest, applierProfile);
        SkillDAO skillDAO = skillMapper.mapToEntity(skill, new CycleAvoidingMappingContext());

        skillManager.saveToDatabase(skillDAO);
        log.info("Skill Created Successfully");
    }
}
