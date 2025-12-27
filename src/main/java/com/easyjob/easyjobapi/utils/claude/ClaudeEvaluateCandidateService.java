package com.easyjob.easyjobapi.utils.claude;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;
import com.easyjob.easyjobapi.core.offerApplicationEvaluation.models.OfferApplicationEvaluationAIResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClaudeEvaluateCandidateService {

    @Value("${anthropic.api.key}")
    private String ANTHROPIC_API_KEY;

    private final ObjectMapper objectMapper;

    @Async
    public CompletableFuture<OfferApplicationEvaluationAIResponse> evaluateCandidate(String prompt) {
        log.info("Claude AI Service starting evaluation");
        try {
            AnthropicClient client = AnthropicOkHttpClient.builder()
                    .apiKey(ANTHROPIC_API_KEY)
                    .build();

            MessageCreateParams createParams = MessageCreateParams.builder()
                    .model(Model.CLAUDE_SONNET_4_20250514)
                    .maxTokens(4096)
                    .addUserMessage(prompt)
                    .build();

            StringBuilder responseBuilder = new StringBuilder();
            client.messages().create(createParams).content().stream()
                    .flatMap(contentBlock -> contentBlock.text().stream())
                    .forEach(textBlock -> responseBuilder.append(textBlock.text()));

            log.info("Received response from Claude AI API");

            String responseString = cleanJsonResponse(responseBuilder.toString());
            log.debug("Cleaned JSON response: {}", responseString);

            OfferApplicationEvaluationAIResponse aiResponse = objectMapper.readValue(
                    responseString,
                    OfferApplicationEvaluationAIResponse.class
            );

            log.info("Claude AI Service completed successfully");
            return CompletableFuture.completedFuture(aiResponse);

        } catch (Exception e) {
            log.error("Failed to evaluate candidate with Claude AI", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    private String cleanJsonResponse(String response) {
        return response
                .replaceAll("```json\\s*", "")
                .replaceAll("```\\s*", "")
                .replaceAll("^json\\s*", "")
                .trim();
    }
}