package com.easyjob.easyjobapi.core.offerApplication.services;

import com.easyjob.easyjobapi.core.offer.models.Offer;
import com.easyjob.easyjobapi.core.offer.services.OfferBuilders;
import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplication;
import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplicationDAO;
import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplicationId;
import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplicationResponse;
import com.easyjob.easyjobapi.core.user.management.UserMapper;
import com.easyjob.easyjobapi.files.storage.services.StorageService;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import com.easyjob.easyjobapi.modules.applierProfile.services.ApplierProfileBuilders;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models.EducationResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.ProjectResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.models.SkillResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.models.WorkExperienceResponse;
import com.easyjob.easyjobapi.utils.enums.ApplicationStatusEnum;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OfferApplicationBuilders {
    public static OfferApplication buildOfferApplication(Offer offer, ApplierProfile applierProfile) {
        return OfferApplication.builder()
                .offerApplicationId(OfferApplicationId.of(null))
                .offer(offer)
                .applierProfile(applierProfile)
                .status(ApplicationStatusEnum.PENDING)
                .isArchived(false)
                .build();
    }

    public static OfferApplicationResponse buildResponse(
            OfferApplicationDAO offerApplication,
            StorageService storageService,
            UserMapper userMapper,
            List<EducationResponse> educationResponses,
            List<ProjectResponse> projectResponses,
            List<SkillResponse> skillResponses,
            List<WorkExperienceResponse> workExperienceResponses
    ) {
        return OfferApplicationResponse.builder()
                .offerApplicationId(offerApplication.getId())
                .offer(OfferBuilders.buildResponse(
                        offerApplication.getOffer(),
                        storageService,
                        userMapper))
                .applierProfile(ApplierProfileBuilders.buildResponse(
                        offerApplication.getApplierProfile(),
                        educationResponses,
                        projectResponses,
                        skillResponses,
                        workExperienceResponses
                ))
                .status(offerApplication.getStatus())
                .build();
    }
}
