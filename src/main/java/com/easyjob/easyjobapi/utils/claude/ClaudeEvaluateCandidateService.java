package com.easyjob.easyjobapi.utils.claude;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ClaudeEvaluateCandidateService {
    @Value("${anthropic.api.key}")
    private String ANTHROPIC_API_KEY;
}
