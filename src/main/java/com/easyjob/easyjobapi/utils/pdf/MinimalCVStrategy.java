package com.easyjob.easyjobapi.utils.pdf;

import com.easyjob.easyjobapi.modules.applierProfile.models.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.*;

@Component
@Slf4j
public class MinimalCVStrategy extends AbstractCVGenerationStrategy {
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 32, Font.NORMAL, BLACK);
    private static final Font SECTION_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BLACK);
    private static final Font SUBTITLE_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, DARK_GRAY);
    private static final Font BODY_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, DARK_GRAY);
    private static final Font SMALL_FONT = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, MEDIUM_GRAY);
    private static final Font SMALL_ITALIC = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC, MEDIUM_GRAY);

    @Override
    public byte[] generatePDF(ApplierProfileCVResponse cvData) throws Exception {
        log.info("Generating Minimal style CV");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = createDocument(60, 60, 60, 60); // Smaller margins for minimal look

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            addHeader(document, cvData.personalInformation());

            if (cvData.summary() != null && !cvData.summary().isEmpty()) {
                addSummary(document, cvData.summary());
            }

            if (cvData.workExperience() != null && !cvData.workExperience().isEmpty()) {
                addWorkExperience(document, cvData.workExperience());
            }

            if (cvData.education() != null && !cvData.education().isEmpty()) {
                addEducation(document, cvData.education());
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
        return "MINIMAL";
    }

    private void addHeader(Document document, ApplierProfileCVPersonalInformationResponse personalInfo)
            throws DocumentException {
        String name = safeString(personalInfo != null ? personalInfo.fullName() : null, "YOUR NAME");
        Paragraph title = new Paragraph(name, TITLE_FONT);
        title.setAlignment(Element.ALIGN_LEFT);
        title.setSpacingAfter(5);
        document.add(title);

        String email = safeString(personalInfo != null ? personalInfo.email() : null, "email@example.com");
        Paragraph contact = new Paragraph(email, SMALL_FONT);
        contact.setAlignment(Element.ALIGN_LEFT);
        contact.setSpacingAfter(10);
        document.add(contact);

        document.add(new Chunk(createSectionLine(LIGHT_GRAY, 0.5f)));
        addSpacing(document, 20);
    }

    private void addSummary(Document document, String summary) throws DocumentException {
        addSectionTitle(document, "About");

        Paragraph summaryPara = new Paragraph(summary, BODY_FONT);
        summaryPara.setAlignment(Element.ALIGN_JUSTIFIED);
        summaryPara.setSpacingAfter(20);
        document.add(summaryPara);
    }

    private void addWorkExperience(Document document, java.util.List<ApplierProfileCVWorkExperienceResponse> workExperiences)
            throws DocumentException {
        addSectionTitle(document, "Experience");

        for (int i = 0; i < workExperiences.size(); i++) {
            ApplierProfileCVWorkExperienceResponse work = workExperiences.get(i);

            PdfPTable headerTable = createBorderlessTable(2);
            try {
                headerTable.setWidths(new float[]{3, 1});
            } catch (DocumentException e) {
                log.warn("Could not set column widths", e);
            }

            PdfPCell leftCell = createBorderlessCell();
            Paragraph jobTitle = new Paragraph(safeString(work.title(), "Position"), SUBTITLE_FONT);
            leftCell.addElement(jobTitle);
            if (work.companyName() != null) {
                Paragraph company = new Paragraph(work.companyName(), BODY_FONT);
                leftCell.addElement(company);
            }
            headerTable.addCell(leftCell);

            PdfPCell rightCell = createBorderlessCell();
            rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            StringBuilder dates = new StringBuilder();
            if (work.startDate() != null) dates.append(work.startDate());
            if (work.endDate() != null) {
                if (dates.length() > 0) dates.append(" - ");
                dates.append(work.endDate());
            }
            if (dates.length() > 0) {
                Paragraph datePara = new Paragraph(dates.toString(), SMALL_FONT);
                datePara.setAlignment(Element.ALIGN_RIGHT);
                rightCell.addElement(datePara);
            }
            if (work.location() != null) {
                Paragraph location = new Paragraph(work.location(), SMALL_FONT);
                location.setAlignment(Element.ALIGN_RIGHT);
                rightCell.addElement(location);
            }
            headerTable.addCell(rightCell);

            document.add(headerTable);

            if (work.responsibilities() != null && !work.responsibilities().isEmpty()) {
                addSpacing(document, 5);
                for (String resp : work.responsibilities()) {
                    Paragraph bullet = new Paragraph("• " + resp, BODY_FONT);
                    bullet.setIndentationLeft(15);
                    bullet.setSpacingAfter(3);
                    document.add(bullet);
                }
            }

            if (i < workExperiences.size() - 1) {
                addSpacing(document, 15);
            }
        }

        addSpacing(document, 20);
    }

    private void addEducation(Document document, java.util.List<ApplierProfileCVEducationResponse> educations)
            throws DocumentException {
        addSectionTitle(document, "Education");

        for (int i = 0; i < educations.size(); i++) {
            ApplierProfileCVEducationResponse edu = educations.get(i);

            PdfPTable headerTable = createBorderlessTable(2);
            try {
                headerTable.setWidths(new float[]{3, 1});
            } catch (DocumentException e) {
                log.warn("Could not set column widths", e);
            }

            PdfPCell leftCell = createBorderlessCell();
            StringBuilder degreeInfo = new StringBuilder();
            if (edu.degree() != null) degreeInfo.append(edu.degree());
            if (edu.major() != null) {
                if (degreeInfo.length() > 0) degreeInfo.append(" in ");
                degreeInfo.append(edu.major());
            }
            if (degreeInfo.length() > 0) {
                Paragraph degree = new Paragraph(degreeInfo.toString(), SUBTITLE_FONT);
                leftCell.addElement(degree);
            }
            if (edu.university() != null) {
                Paragraph university = new Paragraph(edu.university(), BODY_FONT);
                leftCell.addElement(university);
            }
            if (edu.gpa() != null) {
                Paragraph gpa = new Paragraph("GPA: " + edu.gpa(), SMALL_FONT);
                leftCell.addElement(gpa);
            }
            headerTable.addCell(leftCell);

            PdfPCell rightCell = createBorderlessCell();
            rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            StringBuilder dates = new StringBuilder();
            if (edu.startDate() != null) dates.append(edu.startDate());
            if (edu.endDate() != null) {
                if (dates.length() > 0) dates.append(" - ");
                dates.append(edu.endDate());
            }
            if (dates.length() > 0) {
                Paragraph datePara = new Paragraph(dates.toString(), SMALL_FONT);
                datePara.setAlignment(Element.ALIGN_RIGHT);
                rightCell.addElement(datePara);
            }
            headerTable.addCell(rightCell);

            document.add(headerTable);

            if (i < educations.size() - 1) {
                addSpacing(document, 12);
            }
        }

        addSpacing(document, 20);
    }

    private void addSkills(Document document, java.util.List<ApplierProfileCVSkillResponse> skills)
            throws DocumentException {
        addSectionTitle(document, "Skills");

        Map<String, java.util.List<String>> skillsByLevel = new HashMap<>();
        for (ApplierProfileCVSkillResponse skill : skills) {
            String level = safeString(skill.level(), "Skills");
            skillsByLevel.computeIfAbsent(level, k -> new ArrayList<>()).add(skill.name());
        }

        for (Map.Entry<String, java.util.List<String>> entry : skillsByLevel.entrySet()) {
            Paragraph levelHeader = new Paragraph(entry.getKey(), SUBTITLE_FONT);
            levelHeader.setSpacingAfter(5);
            document.add(levelHeader);

            String skillList = String.join("  •  ", entry.getValue());
            Paragraph skillsParagraph = new Paragraph(skillList, BODY_FONT);
            skillsParagraph.setSpacingAfter(12);
            document.add(skillsParagraph);
        }

        addSpacing(document, 8);
    }

    private void addProjects(Document document, java.util.List<ApplierProfileCVProjectResponse> projects)
            throws DocumentException {
        addSectionTitle(document, "Projects");

        for (int i = 0; i < projects.size(); i++) {
            ApplierProfileCVProjectResponse project = projects.get(i);

            if (project.name() != null) {
                Paragraph projectName = new Paragraph(project.name(), SUBTITLE_FONT);
                projectName.setSpacingAfter(3);
                document.add(projectName);
            }

            if (project.description() != null && !project.description().isEmpty()) {
                Paragraph description = new Paragraph(project.description(), BODY_FONT);
                description.setAlignment(Element.ALIGN_JUSTIFIED);
                description.setSpacingAfter(3);
                document.add(description);
            }

            if (project.technologies() != null && !project.technologies().isEmpty()) {
                String techList = String.join("  •  ", project.technologies());
                Paragraph tech = new Paragraph(techList, SMALL_FONT);
                tech.setSpacingAfter(3);
                document.add(tech);
            }

            if (project.link() != null && !project.link().isEmpty()) {
                Paragraph link = new Paragraph(project.link(), SMALL_ITALIC);
                link.setSpacingAfter(12);
                document.add(link);
            }

            if (i < projects.size() - 1) {
                addSpacing(document, 8);
            }
        }
    }

    private void addSectionTitle(Document document, String title) throws DocumentException {
        Paragraph heading = new Paragraph(title.toUpperCase(), SECTION_FONT);
        heading.setSpacingAfter(10);
        heading.setSpacingBefore(5);
        document.add(heading);
    }
}