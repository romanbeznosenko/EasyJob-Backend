package com.easyjob.easyjobapi.modules.applierProfile.skill.services;

import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.modules.applierProfile.skill.management.SkillManager;
import com.easyjob.easyjobapi.modules.applierProfile.skill.management.SkillNotFoundException;
import com.easyjob.easyjobapi.modules.applierProfile.skill.management.SkillUserMismatchException;
import com.easyjob.easyjobapi.modules.applierProfile.skill.models.SkillDAO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class SkillDeleteService {
    private final HttpServletRequest request;
    private final SkillManager skillManager;

    public void delete(UUID skillId){
        log.info("Skill Delete Request");

        UserDAO user = (UserDAO) request.getAttribute("user");
        SkillDAO skillDAO = skillManager.findBySkillId(skillId)
                .orElseThrow(SkillNotFoundException::new);

        if (!skillDAO.getApplierProfile().getUser().getId().equals(user.getId())) {
            throw new SkillUserMismatchException();
        }

        skillDAO.setIsArchived(true);
        skillDAO.setArchivedAt(Instant.now());

        skillManager.saveToDatabase(skillDAO);
        log.info("Skill Deleted successfully");
    }
}
