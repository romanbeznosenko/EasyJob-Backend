package com.easyjob.easyjobapi.modules.applierProfile.skill.services;

import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileManager;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import com.easyjob.easyjobapi.modules.applierProfile.skill.management.SkillManager;
import com.easyjob.easyjobapi.modules.applierProfile.skill.management.SkillMapper;
import com.easyjob.easyjobapi.modules.applierProfile.skill.models.SkillDAO;
import com.easyjob.easyjobapi.modules.applierProfile.skill.models.SkillPageResponse;
import com.easyjob.easyjobapi.modules.applierProfile.skill.models.SkillResponse;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class SkillGetService {
    private final HttpServletRequest request;
    private final SkillManager skillManager;
    private final SkillMapper skillMapper;
    private final ApplierProfileManager applierProfileManager;

    public SkillPageResponse get(int page, int limit){
        log.info("Skill Get request");

        UserDAO userDAO = (UserDAO) request.getAttribute("user");
        ApplierProfileDAO applierProfileDAO = applierProfileManager.findByUser(userDAO.getId())
                .orElseThrow(ApplierProfileNotFoundException::new);

        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<SkillDAO> skillDAOPage = skillManager.findByApplierProfile_Id(applierProfileDAO.getId(), pageable);

        List<SkillResponse> skillResponses = skillDAOPage.get()
                .filter(item -> item.getIsArchived() == false)
                .map(item -> skillMapper.mapToResponse(item, new CycleAvoidingMappingContext()))
                .toList();

        return SkillPageResponse.builder()
                .count(skillDAOPage.getTotalElements())
                .data(skillResponses)
                .build();
    }
}
