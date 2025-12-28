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
public class CreativeCVStrategy extends AbstractCVGenerationStrategy {
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 34, Font.BOLD, PURPLE);
    private static final Font SECTION_FONT = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, ORANGE);
    private static final Font SUBTITLE_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, DARK_GRAY);
    private static final Font BODY_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, DARK_GRAY);
    private static final Font SMALL_FONT = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL, MEDIUM_GRAY);
    private static final Font ACCENT_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, MEDIUM_BLUE);

    @Override
    public byte[] generatePDF(ApplierProfileCVResponse cvData) throws Exception {
        log.info("Generating Creative style CV");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = createDocument(50, 50, 50, 50);

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            addHeader(document, cvData.personalInformation());

            if (cvData.summary() != null && !cvData.summary().isEmpty()) {
                addSummary(document, cvData.summary());
            }

            PdfPTable mainLayout = createBorderlessTable(2);
            try {
                mainLayout.setWidths(new float[]{2, 3});
            } catch (DocumentException e) {
                log.warn("Could not set column widths", e);
            }
            mainLayout.setWidthPercentage(100);

            PdfPCell leftColumn = createBorderlessCell();
            leftColumn.setPaddingRight(15);

            if (cvData.skills() != null && !cvData.skills().isEmpty()) {
                addSkillsToCell(leftColumn, cvData.skills());
            }

            if (cvData.education() != null && !cvData.education().isEmpty()) {
                addEducationToCell(leftColumn, cvData.education());
            }

            mainLayout.addCell(leftColumn);

            PdfPCell rightColumn = createBorderlessCell();
            rightColumn.setPaddingLeft(15);

            if (cvData.workExperience() != null && !cvData.workExperience().isEmpty()) {
                addWorkExperienceToCell(rightColumn, cvData.workExperience());
            }

            if (cvData.projects() != null && !cvData.projects().isEmpty()) {
                addProjectsToCell(rightColumn, cvData.projects());
            }

            mainLayout.addCell(rightColumn);

            document.add(mainLayout);

            return documentToBytes(document, baos);

        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }

    @Override
    public String getStyleName() {
        return "CREATIVE";
    }

    private void addHeader(Document document, ApplierProfileCVPersonalInformationResponse personalInfo)
            throws DocumentException {
        PdfPTable headerTable = new PdfPTable(1);
        headerTable.setWidthPercentage(100);

        PdfPCell headerCell = createColoredCell(LIGHT_BLUE);
        headerCell.setPadding(20);

        String name = safeString(personalInfo != null ? personalInfo.fullName() : null, "YOUR NAME");
        Paragraph title = new Paragraph(name, TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        headerCell.addElement(title);

        String email = safeString(personalInfo != null ? personalInfo.email() : null, "email@example.com");
        Paragraph contact = new Paragraph(email, ACCENT_FONT);
        contact.setAlignment(Element.ALIGN_CENTER);
        contact.setSpacingBefore(5);
        headerCell.addElement(contact);

        headerTable.addCell(headerCell);
        document.add(headerTable);

        addSpacing(document, 20);
    }

    private void addSummary(Document document, String summary) throws DocumentException {
        PdfPTable summaryTable = new PdfPTable(1);
        summaryTable.setWidthPercentage(100);

        PdfPCell summaryCell = createColoredCell(new BaseColor(255, 250, 240));
        summaryCell.setPadding(15);
        summaryCell.setBorderColor(ORANGE);
        summaryCell.setBorder(Rectangle.LEFT);
        summaryCell.setBorderWidth(4);

        Paragraph summaryPara = new Paragraph(summary, BODY_FONT);
        summaryPara.setAlignment(Element.ALIGN_JUSTIFIED);
        summaryCell.addElement(summaryPara);

        summaryTable.addCell(summaryCell);
        document.add(summaryTable);

        addSpacing(document, 20);
    }

    private void addSkillsToCell(PdfPCell cell, java.util.List<ApplierProfileCVSkillResponse> skills) {
        Paragraph sectionTitle = new Paragraph("SKILLS", SECTION_FONT);
        sectionTitle.setSpacingAfter(10);
        cell.addElement(sectionTitle);

        Map<String, java.util.List<String>> skillsByLevel = new HashMap<>();
        for (ApplierProfileCVSkillResponse skill : skills) {
            String level = safeString(skill.level(), "Skills");
            skillsByLevel.computeIfAbsent(level, k -> new ArrayList<>()).add(skill.name());
        }

        for (Map.Entry<String, java.util.List<String>> entry : skillsByLevel.entrySet()) {
            Paragraph levelHeader = new Paragraph(entry.getKey(), SUBTITLE_FONT);
            levelHeader.setSpacingBefore(5);
            levelHeader.setSpacingAfter(5);
            cell.addElement(levelHeader);

            for (String skillName : entry.getValue()) {
                Paragraph skill = new Paragraph("▪ " + skillName, BODY_FONT);
                skill.setSpacingAfter(3);
                cell.addElement(skill);
            }
        }

        Paragraph spacer = new Paragraph(" ");
        spacer.setSpacingBefore(15);
        cell.addElement(spacer);
    }

    private void addEducationToCell(PdfPCell cell, java.util.List<ApplierProfileCVEducationResponse> educations) {
        Paragraph sectionTitle = new Paragraph("EDUCATION", SECTION_FONT);
        sectionTitle.setSpacingAfter(10);
        cell.addElement(sectionTitle);

        for (ApplierProfileCVEducationResponse edu : educations) {
            StringBuilder degreeInfo = new StringBuilder();
            if (edu.degree() != null) degreeInfo.append(edu.degree());
            if (edu.major() != null) {
                if (degreeInfo.length() > 0) degreeInfo.append(" in ");
                degreeInfo.append(edu.major());
            }

            if (degreeInfo.length() > 0) {
                Paragraph degree = new Paragraph(degreeInfo.toString(), SUBTITLE_FONT);
                degree.setSpacingBefore(5);
                degree.setSpacingAfter(3);
                cell.addElement(degree);
            }

            if (edu.university() != null) {
                Paragraph university = new Paragraph(edu.university(), BODY_FONT);
                university.setSpacingAfter(3);
                cell.addElement(university);
            }

            StringBuilder dates = new StringBuilder();
            if (edu.startDate() != null) dates.append(edu.startDate());
            if (edu.endDate() != null) {
                if (dates.length() > 0) dates.append(" - ");
                dates.append(edu.endDate());
            }
            if (dates.length() > 0) {
                Paragraph datePara = new Paragraph(dates.toString(), SMALL_FONT);
                datePara.setSpacingAfter(3);
                cell.addElement(datePara);
            }

            if (edu.gpa() != null) {
                Paragraph gpa = new Paragraph("GPA: " + edu.gpa(), ACCENT_FONT);
                gpa.setSpacingAfter(10);
                cell.addElement(gpa);
            }
        }
    }

    private void addWorkExperienceToCell(PdfPCell cell, java.util.List<ApplierProfileCVWorkExperienceResponse> workExperiences) {
        Paragraph sectionTitle = new Paragraph("EXPERIENCE", SECTION_FONT);
        sectionTitle.setSpacingAfter(10);
        cell.addElement(sectionTitle);

        for (int i = 0; i < workExperiences.size(); i++) {
            ApplierProfileCVWorkExperienceResponse work = workExperiences.get(i);

            Paragraph jobTitle = new Paragraph(safeString(work.title(), "Position"), SUBTITLE_FONT);
            jobTitle.setSpacingBefore(i > 0 ? 10 : 0);
            jobTitle.setSpacingAfter(3);
            cell.addElement(jobTitle);

            if (work.companyName() != null) {
                Paragraph company = new Paragraph(work.companyName(), ACCENT_FONT);
                company.setSpacingAfter(3);
                cell.addElement(company);
            }

            StringBuilder details = new StringBuilder();
            if (work.startDate() != null) details.append(work.startDate());
            if (work.endDate() != null) {
                if (details.length() > 0) details.append(" - ");
                details.append(work.endDate());
            }
            if (work.location() != null) {
                if (details.length() > 0) details.append(" | ");
                details.append(work.location());
            }

            if (details.length() > 0) {
                Paragraph detailsPara = new Paragraph(details.toString(), SMALL_FONT);
                detailsPara.setSpacingAfter(5);
                cell.addElement(detailsPara);
            }

            if (work.responsibilities() != null && !work.responsibilities().isEmpty()) {
                for (String resp : work.responsibilities()) {
                    Paragraph bullet = new Paragraph("◆ " + resp, BODY_FONT);
                    bullet.setIndentationLeft(10);
                    bullet.setSpacingAfter(3);
                    cell.addElement(bullet);
                }
            }
        }
    }

    private void addProjectsToCell(PdfPCell cell, java.util.List<ApplierProfileCVProjectResponse> projects) {
        Paragraph spacer = new Paragraph(" ");
        spacer.setSpacingBefore(15);
        cell.addElement(spacer);

        Paragraph sectionTitle = new Paragraph("PROJECTS", SECTION_FONT);
        sectionTitle.setSpacingAfter(10);
        cell.addElement(sectionTitle);

        for (int i = 0; i < projects.size(); i++) {
            ApplierProfileCVProjectResponse project = projects.get(i);

            if (project.name() != null) {
                Paragraph projectName = new Paragraph(project.name(), SUBTITLE_FONT);
                projectName.setSpacingBefore(i > 0 ? 8 : 0);
                projectName.setSpacingAfter(3);
                cell.addElement(projectName);
            }

            if (project.description() != null && !project.description().isEmpty()) {
                Paragraph description = new Paragraph(project.description(), BODY_FONT);
                description.setAlignment(Element.ALIGN_JUSTIFIED);
                description.setSpacingAfter(3);
                cell.addElement(description);
            }

            if (project.technologies() != null && !project.technologies().isEmpty()) {
                String techList = String.join(" • ", project.technologies());
                Paragraph tech = new Paragraph("Tech: " + techList, SMALL_FONT);
                tech.setSpacingAfter(3);
                cell.addElement(tech);
            }

            if (project.link() != null && !project.link().isEmpty()) {
                Paragraph link = new Paragraph("🔗 " + project.link(), ACCENT_FONT);
                link.setSpacingAfter(5);
                cell.addElement(link);
            }
        }
    }
}