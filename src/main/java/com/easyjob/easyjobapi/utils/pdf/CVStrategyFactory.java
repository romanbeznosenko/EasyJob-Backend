package com.easyjob.easyjobapi.utils.pdf;

import com.easyjob.easyjobapi.utils.enums.CVTemplateEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CVStrategyFactory {
    private final Map<String, CVGenerationStrategy> strategyMap;

    public CVStrategyFactory(List<CVGenerationStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(
                        CVGenerationStrategy::getStyleName,
                        Function.identity()
                ));

        log.info("Registered {} CV generation strategies: {}",
                strategyMap.size(), strategyMap.keySet());
    }

    public CVGenerationStrategy getStrategy(CVTemplateEnum template) {
        if (template == null) {
            log.warn("Template is null, using default MODERN");
            return getStrategy(CVTemplateEnum.MODERN);
        }

        return getStrategy(template.name());
    }

    public CVGenerationStrategy getStrategy(String styleName) {
        CVGenerationStrategy strategy = strategyMap.get(styleName);

        if (strategy == null) {
            log.error("No CV strategy found for style: {}. Available: {}",
                    styleName, strategyMap.keySet());
            throw new IllegalArgumentException(
                    "Unknown CV style: " + styleName +
                            ". Available styles: " + strategyMap.keySet());
        }

        log.debug("Selected CV strategy: {}", styleName);
        return strategy;
    }

    public List<String> getAvailableStyles() {
        return List.copyOf(strategyMap.keySet());
    }

    public boolean hasStrategy(String styleName) {
        return strategyMap.containsKey(styleName);
    }

    public boolean hasStrategy(CVTemplateEnum template) {
        return template != null && strategyMap.containsKey(template.name());
    }
}