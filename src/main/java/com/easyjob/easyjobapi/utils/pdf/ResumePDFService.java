package com.easyjob.easyjobapi.utils.pdf;

import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileCVResponse;
import com.easyjob.easyjobapi.utils.enums.CVTemplateEnum;
import com.easyjob.easyjobapi.utils.pdf.strategy.CVGenerationStrategy;
import com.easyjob.easyjobapi.utils.pdf.strategy.CVStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Main service for CV/Resume PDF generation
 * Uses Strategy Pattern to support multiple CV styles
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ResumePDFService {

    private final CVStrategyFactory strategyFactory;

    /**
     * Generate PDF using specified template style
     *
     * @param cvData The CV data
     * @param template The desired CV style (MODERN, CREATIVE, CORPORATE, MINIMAL)
     * @return PDF bytes
     * @throws Exception if generation fails
     */
    public byte[] generatePDF(ApplierProfileCVResponse cvData, CVTemplateEnum template) throws Exception {
        log.info("Generating PDF with template: {}", template);

        if (cvData == null) {
            throw new IllegalArgumentException("CV data cannot be null");
        }

        if (template == null) {
            log.warn("Template is null, using default MODERN style");
            template = CVTemplateEnum.MODERN;
        }

        CVGenerationStrategy strategy = strategyFactory.getStrategy(template);
        byte[] pdfBytes = strategy.generatePDF(cvData);

        log.info("PDF generated successfully with template: {}, size: {} bytes",
                template, pdfBytes.length);

        return pdfBytes;
    }

    /**
     * Generate PDF using default (MODERN) template
     *
     * @param cvData The CV data
     * @return PDF bytes
     * @throws Exception if generation fails
     */
    public byte[] generatePDF(ApplierProfileCVResponse cvData) throws Exception {
        return generatePDF(cvData, CVTemplateEnum.MODERN);
    }

    /**
     * Get list of all available CV template styles
     *
     * @return List of available style names
     */
    public java.util.List<String> getAvailableTemplates() {
        return strategyFactory.getAvailableStyles();
    }
}