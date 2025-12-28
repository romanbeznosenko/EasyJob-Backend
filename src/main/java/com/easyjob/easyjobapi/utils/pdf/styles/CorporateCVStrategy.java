package com.easyjob.easyjobapi.utils.pdf.styles;

import com.easyjob.easyjobapi.modules.applierProfile.models.*;
import com.easyjob.easyjobapi.utils.pdf.strategy.AbstractCVGenerationStrategy;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.List;

@Component
@Slf4j
public class CorporateCVStrategy extends AbstractCVGenerationStrategy {
    private static final Font TITLE_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 26, Font.BOLD, BLACK);
    private static final Font SECTION_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BLACK);
    private static final Font SUBTITLE_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private static final Font BODY_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);
    private static final Font SMALL_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, DARK_GRAY);
    private static final Font SMALL_ITALIC = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.ITALIC, DARK_GRAY);

    @Override
    public byte[] generatePDF(ApplierProfileCVResponse cvData) throws Exception {
        log.info("Generating Corporate style CV");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = createDocument();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            addHeader(document, cvData.personalInformation());

            if (cvData.summary() != null && !cvData.summary().isEmpty()) {
                addSummary(document, cvData.summary());
            }

            if (cvData.education() != null && !cvData.education().isEmpty()) {
                addEducation(document, cvData.education());
            }

            if (cvData.workExperience() != null && !cvData.workExperience().isEmpty()) {
                addWorkExperience(document, cvData.workExperience());
            }

            if (cvData.skills() != null && !cvData.skills().isEmpty()) {
                addSkills(document, cvData.skills());
            }

            if (cvData.projects() != null && !cvData.projects().isEmpty()) {
                addProjects(document, cvData.projects());
            }

            return documentToBytes(document, baos);

        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }

    @Override
    public String getStyleName() {
        return "CORPORATE";
    }

    private void addHeader(Document document, ApplierProfileCVPersonalInformationResponse personalInfo)
            throws DocumentException {
        String name = safeString(personalInfo != null ? personalInfo.fullName() : null, "YOUR NAME");
        Paragraph title = new Paragraph(name.toUpperCase(), TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(5);
        document.add(title);

        String email = safeString(personalInfo != null ? personalInfo.email() : null, "email@example.com");
        Paragraph contact = new Paragraph(email, SMALL_FONT);
        contact.setAlignment(Element.ALIGN_CENTER);
        contact.setSpacingAfter(15);
        document.add(contact);

        document.add(new Chunk(createSectionLine(BLACK, 2)));
        addSpacing(document, 15);
    }

    private void addSummary(Document document, String summary) throws DocumentException {
        addSectionTitle(document, "PROFESSIONAL OBJECTIVE");
        Paragraph summaryPara = new Paragraph(summary, BODY_FONT);
        summaryPara.setAlignment(Element.ALIGN_JUSTIFIED);
        summaryPara.setSpacingAfter(15);
        document.add(summaryPara);
    }

    private void addEducation(Document document, java.util.List<ApplierProfileCVEducationResponse> educations)
            throws DocumentException {
        addSectionTitle(document, "EDUCATION");

        for (ApplierProfileCVEducationResponse edu : educations) {
            StringBuilder degreeInfo = new StringBuilder();
            if (edu.degree() != null) degreeInfo.append(edu.degree());
            if (edu.major() != null) {
                if (degreeInfo.length() > 0) degreeInfo.append(", ");
                degreeInfo.append(edu.major());
            }

            if (degreeInfo.length() > 0) {
                Paragraph degree = new Paragraph(degreeInfo.toString(), SUBTITLE_FONT);
                degree.setSpacingAfter(3);
                document.add(degree);
            }

            if (edu.university() != null) {
                Paragraph university = new Paragraph(edu.university(), BODY_FONT);
                university.setSpacingAfter(3);
                document.add(university);
            }

            StringBuilder dates = new StringBuilder();
            if (edu.startDate() != null) dates.append(edu.startDate());
            if (edu.endDate() != null) {
                if (dates.length() > 0) dates.append(" - ");
                dates.append(edu.endDate());
            }
            if (dates.length() > 0) {
                Paragraph datePara = new Paragraph(dates.toString(), SMALL_ITALIC);
                datePara.setSpacingAfter(3);
                document.add(datePara);
            }

            if (edu.gpa() != null) {
                Paragraph gpa = new Paragraph("GPA: " + edu.gpa(), SMALL_FONT);
                gpa.setSpacingAfter(12);
                document.add(gpa);
            } else {
                addSpacing(document, 12);
            }
        }
    }

    private void addWorkExperience(Document document, List<ApplierProfileCVWorkExperienceResponse> workExperiences)
            throws DocumentException {
        addSectionTitle(document, "PROFESSIONAL EXPERIENCE");

        for (ApplierProfileCVWorkExperienceResponse work : workExperiences) {
            Paragraph jobTitle = new Paragraph(safeString(work.title()), SUBTITLE_FONT);
            jobTitle.setSpacingAfter(3);
            document.add(jobTitle);

            if (work.companyName() != null) {
                Paragraph company = new Paragraph(work.companyName(), BODY_FONT);
                company.setSpacingAfter(3);
                document.add(company);
            }

            StringBuilder details = new StringBuilder();
            if (work.startDate() != null) details.append(work.startDate());
            if (work.endDate() != null) {
                if (details.length() > 0) details.append(" - ");
                details.append(work.endDate());
            }
            if (work.location() != null) {
                if (details.length() > 0) details.append(" • ");
                details.append(work.location());
            }

            if (details.length() > 0) {
                Paragraph detailsPara = new Paragraph(details.toString(), SMALL_ITALIC);
                detailsPara.setSpacingAfter(5);
                document.add(detailsPara);
            }

            if (work.responsibilities() != null && !work.responsibilities().isEmpty()) {
                com.itextpdf.text.List list = createBulletList("•", 30);
                for (String resp : work.responsibilities()) {
                    ListItem item = new ListItem(resp, BODY_FONT);
                    item.setAlignment(Element.ALIGN_JUSTIFIED);
                    list.add(item);
                }
                document.add(list);
            }

            addSpacing(document, 12);
        }
    }

    private void addSkills(Document document, List<ApplierProfileCVSkillResponse> skills)
            throws DocumentException {
        addSectionTitle(document, "CORE COMPETENCIES");

        Map<String, java.util.List<String>> skillsByLevel = new HashMap<>();
        for (ApplierProfileCVSkillResponse skill : skills) {
            String level = safeString(skill.level(), "General");
            skillsByLevel.computeIfAbsent(level, k -> new ArrayList<>()).add(skill.name());
        }

        for (Map.Entry<String, java.util.List<String>> entry : skillsByLevel.entrySet()) {
            Paragraph levelHeader = new Paragraph(entry.getKey() + ":", SUBTITLE_FONT);
            levelHeader.setSpacingAfter(5);
            document.add(levelHeader);

            String skillList = String.join(" • ", entry.getValue());
            Paragraph skillsParagraph = new Paragraph(skillList, BODY_FONT);
            skillsParagraph.setSpacingAfter(12);
            document.add(skillsParagraph);
        }
    }

    private void addProjects(Document document, java.util.List<ApplierProfileCVProjectResponse> projects)
            throws DocumentException {
        addSectionTitle(document, "KEY PROJECTS");

        for (ApplierProfileCVProjectResponse project : projects) {
            if (project.name() != null) {
                Paragraph projectName = new Paragraph(project.name(), SUBTITLE_FONT);
                projectName.setSpacingAfter(3);
                document.add(projectName);
            }

            if (project.description() != null && !project.description().isEmpty()) {
                Paragraph description = new Paragraph(project.description(), BODY_FONT);
                description.setAlignment(Element.ALIGN_JUSTIFIED);
                description.setSpacingAfter(5);
                document.add(description);
            }

            if (project.technologies() != null && !project.technologies().isEmpty()) {
                String techList = String.join(" • ", project.technologies());
                Paragraph tech = new Paragraph("Technologies: " + techList, SMALL_FONT);
                tech.setSpacingAfter(12);
                document.add(tech);
            } else {
                addSpacing(document, 12);
            }
        }
    }

    private void addSectionTitle(Document document, String title) throws DocumentException {
        addSpacing(document, 8);

        Paragraph heading = new Paragraph(title.toUpperCase(), SECTION_FONT);
        heading.setSpacingAfter(8);
        document.add(heading);

        document.add(new Chunk(createSectionLine(BLACK, 1)));
        addSpacing(document, 8);
    }
}