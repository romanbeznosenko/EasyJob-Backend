package com.easyjob.easyjobapi.utils.claude;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.core.MultipartField;
import com.anthropic.models.beta.AnthropicBeta;
import com.anthropic.models.beta.files.FileMetadata;
import com.anthropic.models.beta.files.FileUploadParams;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileCVModificationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ClaudeCVModificationService {
    @Value("${anthropic.api.key}")
    private String ANTHROPIC_API_KEY;

    @Async
    public CompletableFuture<ApplierProfileCVModificationResponse> modifyCV(
            String prompt,
            byte[] cvBytes,
            String cvFilename
    ) {
        log.info("Claude AI Service modifying CV based on job offer");
        try {
            AnthropicClient client = AnthropicOkHttpClient.builder()
                    .apiKey(ANTHROPIC_API_KEY)
                    .build();

            String sanitizedFilename = sanitizeFilename(cvFilename);
            log.info("Sanitized filename: {} -> {}", cvFilename, sanitizedFilename);

            log.info("Uploading CV file to Anthropic Files API");
            InputStream cvInputStream = new ByteArrayInputStream(cvBytes);

            FileUploadParams uploadParams = FileUploadParams.builder()
                    .file(MultipartField.<InputStream>builder()
                            .value(cvInputStream)
                            .filename(sanitizedFilename)
                            .contentType("application/pdf")
                            .build())
                    .addBeta(AnthropicBeta.FILES_API_2025_04_14)
                    .build();

            FileMetadata fileMetadata = client.beta().files().upload(uploadParams);
            log.info("CV file uploaded successfully with ID: {}", fileMetadata.id());

            String enrichedPrompt = String.format(
                    "I have attached a CV file (ID: %s). %s",
                    fileMetadata.id(),
                    prompt
            );

            MessageCreateParams createParams = MessageCreateParams.builder()
                    .model(Model.CLAUDE_SONNET_4_20250514)
                    .maxTokens(4096)
                    .addUserMessage(enrichedPrompt)
                    .build();

            log.info("Sending request to Claude API");

            StringBuilder responseBuilder = new StringBuilder();
            client.messages().create(createParams).content().stream()
                    .flatMap(contentBlock -> contentBlock.text().stream())
                    .forEach(textBlock -> responseBuilder.append(textBlock.text()));

            log.info("Received response from Claude AI API");

            String responseString = cleanJsonResponse(responseBuilder.toString());
            log.debug("Cleaned JSON response: {}", responseString);

            ObjectMapper mapper = new ObjectMapper();
            ApplierProfileCVModificationResponse cvResponse = mapper.readValue(
                    responseString,
                    ApplierProfileCVModificationResponse.class
            );

            log.info("CV modification completed successfully");
            return CompletableFuture.completedFuture(cvResponse);

        } catch (Exception e) {
            log.error("Failed to modify CV with Claude AI", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    private String sanitizeFilename(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "cv.pdf";
        }

        String[] parts = filename.split("/");
        String actualFilename = parts[parts.length - 1];
        actualFilename = actualFilename.replaceAll("[^a-zA-Z0-9._-]", "_");

        if (!actualFilename.toLowerCase().endsWith(".pdf")) {
            actualFilename = actualFilename + ".pdf";
        }

        return actualFilename;
    }

    private String cleanJsonResponse(String response) {
        return response
                .replaceAll("```json\\s*", "")
                .replaceAll("```\\s*", "")
                .replaceAll("^json\\s*", "")
                .trim();
    }
}
