package com.easyjob.easyjobapi.core.offerApplicationEvaluation.models;

import java.util.List;

public record SkillsAnalysisResponse(
        List<String> requiredSkills,
        List<String> candidateHas,
        List<String> missingSkills,
        List<String>transferableSkills
) {
}
