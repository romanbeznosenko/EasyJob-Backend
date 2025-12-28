package com.easyjob.easyjobapi.core.offerApplication.services;

import com.easyjob.easyjobapi.core.offerApplication.management.OfferApplicationManager;
import com.easyjob.easyjobapi.core.offerApplication.management.OfferApplicationSpecifications;
import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplicationDAO;
import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplicationPageResponse;
import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplicationResponse;
import com.easyjob.easyjobapi.core.user.management.UserMapper;
import com.easyjob.easyjobapi.core.user.management.UserNotRecruiterException;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.files.storage.services.StorageService;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.management.EducationManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.management.EducationMapper;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models.EducationDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models.EducationResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.management.ProjectManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.management.ProjectMapper;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.ProjectDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.ProjectResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.management.SkillManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.management.SkillMapper;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.models.SkillDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.models.SkillResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.management.WorkExperienceManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.management.WorkExperienceMapper;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.models.WorkExperienceDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.models.WorkExperienceResponse;
import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
import com.easyjob.easyjobapi.utils.enums.UserTypeEnum;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OfferApplicationListFirmService {
    private final HttpServletRequest request;
    private final OfferApplicationManager offerApplicationManager;
    private final EducationManager educationManager;
    private final EducationMapper educationMapper;
    private final ProjectManager projectManager;
    private final ProjectMapper projectMapper;
    private final SkillManager skillManager;
    private final SkillMapper skillMapper;
    private final WorkExperienceManager workExperienceManager;
    private final WorkExperienceMapper workExperienceMapper;
    private final StorageService storageService;
    private final UserMapper userMapper;

    public OfferApplicationPageResponse getOfferApplicationList(int page, int limit){
        log.info("Get offer applications for logged in firm");
        UserDAO userDAO = (UserDAO) request.getAttribute("user");
        if (!userDAO.getUserType().equals(UserTypeEnum.RECRUITER)) {
            throw new UserNotRecruiterException();
        }

        Pageable pageable = PageRequest.of(page - 1, limit);
        Specification<OfferApplicationDAO> specification = OfferApplicationSpecifications.findByUser(userDAO);

        Page<OfferApplicationDAO> offerApplicationDAOS = offerApplicationManager.findAll(specification, pageable);
        List<OfferApplicationResponse> offerApplicationResponses = offerApplicationDAOS.get()
                .map(item -> {
                    Pageable pageableApplierProfile = PageRequest.of(0, Integer.MAX_VALUE);
                    Page<EducationDAO> educationPage = educationManager.findByApplierProfile_Id(item.getApplierProfile().getId(), pageableApplierProfile);
                    Page<ProjectDAO> projectPage = projectManager.findByApplierProfile_Id(item.getApplierProfile().getId(), pageableApplierProfile);
                    Page<SkillDAO> skillPage = skillManager.findByApplierProfile_Id(item.getApplierProfile().getId(), pageableApplierProfile);
                    Page<WorkExperienceDAO> workExperiencePage = workExperienceManager.findByApplierProfile(item.getApplierProfile().getId(), pageableApplierProfile);

                    List<EducationResponse> educationResponses = educationPage.get()
                            .filter(educationDAO -> educationDAO.getIsArchived() == false)
                            .map(educationDAO -> educationMapper.mapToResponse(educationDAO, new CycleAvoidingMappingContext()))
                            .toList();

                    List<ProjectResponse> projectResponses = projectPage.get()
                            .filter(projectDAO -> projectDAO.getIsArchived() == false)
                            .map(projectDAO -> projectMapper.mapToResponse(projectDAO, new CycleAvoidingMappingContext()))
                            .toList();

                    List<SkillResponse> skillResponses = skillPage.get()
                            .filter(skillDAO -> skillDAO.getIsArchived() == false)
                            .map(skillDAO -> skillMapper.mapToResponse(skillDAO, new CycleAvoidingMappingContext()))
                            .toList();

                    List<WorkExperienceResponse> workExperienceResponses = workExperiencePage.get()
                            .filter(workExperienceDAO -> workExperienceDAO.getIsArchived() == false)
                            .map(workExperienceDAO -> workExperienceMapper.mapToResponse(workExperienceDAO, new CycleAvoidingMappingContext()))
                            .toList();

                    return OfferApplicationBuilders.buildResponse(
                            item,
                            storageService,
                            userMapper,
                            educationResponses,
                            projectResponses,
                            skillResponses,
                            workExperienceResponses
                    );
                })
                .toList();

        return OfferApplicationPageResponse.builder()
                .count(offerApplicationDAOS.getTotalElements())
                .data(offerApplicationResponses)
                .build();
    }
}
