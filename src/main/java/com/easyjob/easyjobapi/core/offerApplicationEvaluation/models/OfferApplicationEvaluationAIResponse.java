package com.easyjob.easyjobapi.core.offerApplicationEvaluation.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OfferApplicationEvaluationAIResponse(
        @JsonProperty("overallScore")
        Integer overallScore,

        @JsonProperty("skillsScore")
        Integer skillsScore,

        @JsonProperty("experienceScore")
        Integer experienceScore,

        @JsonProperty("educationScore")
        Integer educationScore,

        @JsonProperty("projectsScore")
        Integer projectsScore,

        @JsonProperty("skillsAnalysis")
        SkillsAnalysisAIResponse skillsAnalysis,

        @JsonProperty("strengths")
        List<String> strengths,

        @JsonProperty("weaknesses")
        List<String> weaknesses,

        @JsonProperty("culturalFit")
        String culturalFit,

        @JsonProperty("growthPotential")
        String growthPotential,

        @JsonProperty("recommendation")
        String recommendation,

        @JsonProperty("interviewFocusAreas")
        List<String> interviewFocusAreas,

        @JsonProperty("detailedSummary")
        String detailedSummary
) {
}
