package com.easyjob.easyjobapi.utils.claude;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileCVModificationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClaudeCVModificationService {
    @Value("${anthropic.api.key}")
    private String ANTHROPIC_API_KEY;

    private final ObjectMapper objectMapper;

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

            log.info("Extracting text from PDF, size: {} bytes", cvBytes.length);
            String cvText = extractTextFromPDF(cvBytes);
            log.info("Extracted text length: {} characters", cvText.length());
            log.info("=== EXTRACTED CV TEXT START ===");
            log.info(cvText);
            log.info("=== EXTRACTED CV TEXT END ===");

            String fullPrompt = String.format(
                    "Here is the CV content extracted from the PDF:\n\n%s\n\n%s",
                    cvText,
                    prompt
            );

            MessageCreateParams createParams = MessageCreateParams.builder()
                    .model(Model.CLAUDE_SONNET_4_20250514)
                    .maxTokens(4096)
                    .addUserMessage(fullPrompt)
                    .build();

            log.info("Sending request to Claude API with CV text");

            StringBuilder responseBuilder = new StringBuilder();
            client.messages().create(createParams).content().stream()
                    .flatMap(contentBlock -> contentBlock.text().stream())
                    .forEach(textBlock -> responseBuilder.append(textBlock.text()));

            log.info("Received response from Claude AI API");

            String rawResponse = responseBuilder.toString();

            String responseString = extractAndCleanJson(rawResponse);

            ApplierProfileCVModificationResponse cvResponse = objectMapper.readValue(
                    responseString,
                    ApplierProfileCVModificationResponse.class
            );

            return CompletableFuture.completedFuture(cvResponse);

        } catch (Exception e) {
            log.error("Failed to modify CV with Claude AI", e);
            log.error("Exception type: {}", e.getClass().getName());
            log.error("Exception message: {}", e.getMessage());
            if (e.getCause() != null) {
                log.error("Cause: {}", e.getCause().getMessage());
            }
            return CompletableFuture.failedFuture(e);
        }
    }

    private String extractTextFromPDF(byte[] pdfBytes) throws Exception {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes);
             PDDocument document = PDDocument.load(inputStream)) {

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            if (text == null || text.trim().isEmpty()) {
                throw new IllegalArgumentException("Could not extract text from PDF - file may be empty or image-based");
            }

            return text.trim();
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

    private String extractAndCleanJson(String response) {
        if (response == null || response.isEmpty()) {
            throw new IllegalArgumentException("Empty response from Claude");
        }

        log.info("Starting JSON extraction...");
        log.info("Original response starts with: {}",
                response.length() > 100 ? response.substring(0, 100) : response);

        String cleaned = response;

        cleaned = cleaned.replaceAll("```json\\s*", "");
        cleaned = cleaned.replaceAll("```\\s*", "");
        cleaned = cleaned.replaceAll("^json\\s*", "");

        int jsonStart = cleaned.indexOf('{');
        int jsonEnd = cleaned.lastIndexOf('}');

        if (jsonStart != -1 && jsonEnd != -1 && jsonEnd > jsonStart) {
            cleaned = cleaned.substring(jsonStart, jsonEnd + 1);
            log.info("Extracted JSON from position {} to {} (length: {} chars)",
                    jsonStart, jsonEnd, cleaned.length());
        } else {
            log.warn("Could not find JSON boundaries in response");
            log.warn("First '{' at position: {}", jsonStart);
            log.warn("Last '}' at position: {}", jsonEnd);
        }

        return cleaned.trim();
    }
}