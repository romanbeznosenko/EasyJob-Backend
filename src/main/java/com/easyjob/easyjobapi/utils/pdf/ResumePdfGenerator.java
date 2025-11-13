package com.easyjob.easyjobapi.utils.pdf;

import com.easyjob.easyjobapi.modules.applierProfile.models.*;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumePdfGenerator {
    private final TemplateEngine templateEngine;

    private static final DateTimeFormatter INPUT_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter OUTPUT_DATE_FORMAT = DateTimeFormatter.ofPattern("MMM yyyy");

    public byte[] generateResumePdf(ApplierProfileCVResponse cvData) throws IOException {
        Map<String, Object> templateData = prepareTemplateData(cvData);

        Context context = new Context();
        context.setVariables(templateData);
        String html = templateEngine.process("resume-template", context);

        return convertHtmlToPdf(html);
    }

    private Map<String, Object> prepareTemplateData(ApplierProfileCVResponse cvData) {
        Map<String, Object> data = new HashMap<>();

        data.put("personalInfo", cvData.personalInformation() != null
                ? cvData.personalInformation()
                : new ApplierProfileCVPersonalInformationResponse("", ""));

        data.put("summary", cvData.summary() != null ? cvData.summary() : "");

        data.put("skills", cvData.skills());

        if (cvData.workExperience() != null) {
            List<WorkExperienceViewModel> workExp = cvData.workExperience().stream()
                    .map(this::toWorkExperienceViewModel)
                    .collect(Collectors.toList());
            data.put("workExperience", workExp);
        }

        if (cvData.education() != null) {
            List<EducationViewModel> education = cvData.education().stream()
                    .map(this::toEducationViewModel)
                    .collect(Collectors.toList());
            data.put("education", education);
        }

        data.put("projects", cvData.projects());

        return data;
    }

    private WorkExperienceViewModel toWorkExperienceViewModel(ApplierProfileCVWorkExperienceResponse exp) {
        return new WorkExperienceViewModel(
                exp.title(),
                exp.companyName(),
                exp.location(),
                formatDateRange(exp.startDate(), exp.endDate()),
                exp.responsibilities()
        );
    }

    private EducationViewModel toEducationViewModel(ApplierProfileCVEducationResponse edu) {
        return new EducationViewModel(
                edu.degree(),
                edu.university(),
                edu.major(),
                formatDateRange(edu.startDate(), edu.endDate()),
                edu.gpa()
        );
    }

    private String formatDateRange(String startDate, String endDate) {
        try {
            String formattedStart = formatDate(startDate);
            String formattedEnd = "PRESENT";

            if (endDate != null && !endDate.isEmpty()) {
                LocalDate end = LocalDate.parse(endDate, INPUT_DATE_FORMAT);
                formattedEnd = end.format(OUTPUT_DATE_FORMAT).toUpperCase();
            }

            return formattedStart + " - " + formattedEnd;
        } catch (DateTimeParseException e) {
            return startDate + " - " + (endDate != null ? endDate : "PRESENT");
        }
    }

    private String formatDate(String date) {
        if (date == null || date.isEmpty()) {
            return "";
        }

        try {
            LocalDate localDate = LocalDate.parse(date, INPUT_DATE_FORMAT);
            return localDate.format(OUTPUT_DATE_FORMAT).toUpperCase();
        } catch (DateTimeParseException e) {
            return date;
        }
    }

    private byte[] convertHtmlToPdf(String html) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();
        }
    }

    public record WorkExperienceViewModel(
            String title,
            String companyName,
            String location,
            String dateRange,
            List<String> responsibilities
    ) {}

    public record EducationViewModel(
            String degree,
            String university,
            String major,
            String dateRange,
            String gpa
    ) {}
}