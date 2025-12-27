package com.easyjob.easyjobapi.core.offerApplicationEvaluation.services;

import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplication;
import com.easyjob.easyjobapi.core.offerApplicationEvaluation.models.OfferApplicationEvaluation;
import com.easyjob.easyjobapi.core.offerApplicationEvaluation.models.OfferApplicationEvaluationId;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import com.easyjob.easyjobapi.utils.enums.ProcessStatusEnum;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OfferApplicationEvaluationBuilders {
    public static OfferApplicationEvaluation build(OfferApplication offerApplication, ApplierProfile applierProfile) {
        return OfferApplicationEvaluation.builder()
                .offerApplicationEvaluationId(OfferApplicationEvaluationId.of(null))
                .offerApplication(offerApplication)
                .applierProfile(applierProfile)
                .processStatus(ProcessStatusEnum.IN_PROGRESS)
                .build();
    }
}
