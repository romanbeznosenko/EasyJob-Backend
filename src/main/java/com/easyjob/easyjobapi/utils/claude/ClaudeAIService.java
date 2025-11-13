package com.easyjob.easyjobapi.utils.claude;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ClaudeAIService {
    @Value("${anthropic.api.key}")
    private String ANTHROPIC_API_KEY;

    public String test() {
        log.info("Claude AI Service test");
        AnthropicClient client = AnthropicOkHttpClient.builder()
                .apiKey(ANTHROPIC_API_KEY)
                .build();

        MessageCreateParams createParams = MessageCreateParams.builder()
                .model(Model.CLAUDE_SONNET_4_20250514)
                .maxTokens(2048)
                .addUserMessage("Tell me a story about building the best SDK!")
                .build();

        StringBuilder responseBuilder = new StringBuilder();

        client.messages().create(createParams).content().stream()
                .flatMap(contentBlock -> contentBlock.text().stream())
                .forEach(textBlock -> responseBuilder.append(textBlock.text()));

        return responseBuilder.toString();
    }
}
