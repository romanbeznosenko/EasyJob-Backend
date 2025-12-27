package com.easyjob.easyjobapi.core.offerApplicationEvaluation.models;

import com.easyjob.easyjobapi.utils.enums.ProcessStatusEnum;
import com.easyjob.easyjobapi.utils.enums.RecommendationEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record OfferApplicationEvaluationResponse(
        @Schema(description = "Evaluation's id", example = "1")
        UUID evaluationId,

        @Schema(description = "Candidate's overall match score (0-100)", example = "100")
        Integer overallMatchScore,

        @Schema(description = "Candidate's skills score (0-100)", example = "100")
        Integer skillsScore,

        @Schema(description = "Candidate's experience score (0-100)", example = "100")
        Integer experienceScore,

        @Schema(description = "Candidate's education score (0-100)", example = "100")
        Integer educationScore,

        @Schema(description = "Candidate's projects score (0-100)", example = "100")
        Integer projectsScore,

        @Schema(description = "Candidate's skills analysis", implementation = SkillsAnalysisResponse.class)
        SkillsAnalysisResponse skillsAnalysis,

        @Schema(description = "Candidate's strengths")
        String strengths,

        @Schema(description = "Candidate's weaknesses")
        String weaknesses,

        @Schema(description = "Candidate's cultural fit")
        String culturalFit,

        @Schema(description = "Candidate's growth potential")
        String growthPotential,

        @Schema(description = "Candidate's interview focus area")
        String interviewFocusArea,

        @Schema(description = "Candidate's detailed summary")
        String detailedSummary,

        @Schema(description = "Candidate's recommendation")
        RecommendationEnum recommendation,

        @Schema(description = "Candidate's evaluation status")
        ProcessStatusEnum processStatus,

        @Schema(description = "Evaluation's timestamp")
        Instant createdAt
) {
}
