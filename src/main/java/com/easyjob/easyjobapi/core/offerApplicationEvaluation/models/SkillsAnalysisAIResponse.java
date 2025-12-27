package com.easyjob.easyjobapi.core.offerApplicationEvaluation.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SkillsAnalysisAIResponse(
        @JsonProperty("requiredSkills")
        List<String> requiredSkills,

        @JsonProperty("candidateHas")
        List<String> candidateHas,

        @JsonProperty("missingSkills")
        List<String> missingSkills,

        @JsonProperty("transferableSkills")
        List<String> transferableSkills
) {
}
