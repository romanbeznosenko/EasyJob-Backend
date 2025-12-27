package com.easyjob.easyjobapi.core.offerApplicationEvaluation.models;

import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplication;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfile;
import com.easyjob.easyjobapi.utils.enums.ApplicationStatusEnum;
import com.easyjob.easyjobapi.utils.enums.ProcessStatusEnum;
import com.easyjob.easyjobapi.utils.enums.RecommendationEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfferApplicationEvaluation {
    private OfferApplicationEvaluationId offerApplicationEvaluationId;
    private OfferApplication offerApplication;
    private ApplierProfile applierProfile;
    private ApplicationStatusEnum status;
    private Integer overAllMatchScore;
    private Integer skillsScore;
    private Integer experienceScore;
    private Integer educationScore;
    private Integer projectScore;
    private String skillsAnalysis;
    private String strengths;
    private String weaknesses;
    private String culturalFit;
    private String growthPotential;
    private String interviewFocusAreas;
    private String detailedSummary;
    private RecommendationEnum recommendation;
    private ProcessStatusEnum processStatus;

    private Instant createdAt;
    private Instant updatedAt;

    private Boolean isArchived;
    private Instant archivedAt;
}
