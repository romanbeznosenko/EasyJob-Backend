package com.easyjob.easyjobapi.utils.claude;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileCVResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ClaudeAIService {
    @Value("${anthropic.api.key}")
    private String ANTHROPIC_API_KEY;

    private final static String PROMPT = """
            You are an expert technical recruiter and resume writer.
            Your task is to transform and enhance the following applicant data into a structured, professional CV JSON suitable for PDF export.
            Requirements:
                1. Output strictly in this JSON format:
                ```
                {
                  "personal_information": {
                    "full_name": "",
                    "email": ""
                  },
                  "education": [
                    {
                      "degree": "",
                      "university": "",
                      "major": "",
                      "start_date": "",
                      "end_date": "",
                      "gpa": ""
                    }
                  ],
                  "work_experience": [
                    {
                      "title": "",
                      "company_name": "",
                      "start_date": "",
                      "end_date": "",
                      "location": "",
                      "responsibilities": [
                        ""
                      ]
                    }
                  ],
                  "projects": [
                    {
                      "name": "",
                      "description": "",
                      "technologies": [],
                      "link": ""
                    }
                  ],
                  "skills": [
                    {
                      "name": "",
                      "level": ""
                    }
                  ],
                  "summary": ""
                }
                ```
                2. Add a “summary” field — a short, professional paragraph summarizing the applicant’s strengths and background.
                3. Enhance job responsibilities and project descriptions:
                    a. Use strong action verbs (Developed, Designed, Implemented, Optimized, Collaborated, Automated).
                    b. Emphasize measurable impact, efficiency, and teamwork.
                    c. If responsibilities are empty, infer realistic duties based on title and technologies.
                4. Keep all factual information (dates, company names, etc.) accurate.
                5. Ensure the final text is professional, fluent, and formatted consistently.
            Data:
            """;

    @Async
    public CompletableFuture<ApplierProfileCVResponse> getApplierProfileCV(String applierProfile) {
        log.info("Claude AI Service generating ApplierProfileCVResponse");
        try {
            AnthropicClient client = AnthropicOkHttpClient.builder()
                    .apiKey(ANTHROPIC_API_KEY)
                    .build();

            MessageCreateParams createParams = MessageCreateParams.builder()
                    .model(Model.CLAUDE_SONNET_4_20250514)
                    .maxTokens(2048)
                    .addUserMessage(PROMPT + applierProfile)
                    .build();

            StringBuilder responseBuilder = new StringBuilder();
            client.messages().create(createParams).content().stream()
                    .flatMap(contentBlock -> contentBlock.text().stream())
                    .forEach(textBlock -> responseBuilder.append(textBlock.text()));
            log.info("Received response from Claude AI API");

            String responseString = responseBuilder.toString()
                    .replace("```", "")
                    .replace("json", "")
                    .trim();

            ObjectMapper mapper = new ObjectMapper();
            ApplierProfileCVResponse cvResponse = mapper.readValue(responseString, ApplierProfileCVResponse.class);
            log.info("Claude AI Service is finished");
            return CompletableFuture.completedFuture(cvResponse);
        } catch (Exception e) {
            log.error("Failed to parse JSON into ApplierProfileCVResponse", e);
            return CompletableFuture.completedFuture(null);
        }
    }

}
