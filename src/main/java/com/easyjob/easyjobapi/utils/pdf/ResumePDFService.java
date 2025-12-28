package com.easyjob.easyjobapi.utils.pdf;

import com.easyjob.easyjobapi.modules.applierProfile.models.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Direct PDF generation using iText library with actual user data
 * No LibreOffice or external dependencies required
 */
@Service
@Slf4j
public class ResumePDFService {

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, new BaseColor(44, 62, 80));
    private static final Font SECTION_FONT = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, new BaseColor(44, 62, 80));
    private static final Font SUBTITLE_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font BODY_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
    private static final Font SMALL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.GRAY);
    private static final Font SMALL_ITALIC = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);

    /**
     * Generate CV as PDF and return as byte array using actual user data
     */
    public byte[] generatePDF(ApplierProfileCVResponse cvData) throws Exception {
        log.info("Starting PDF generation with user data");

        if (cvData == null) {
            throw new IllegalArgumentException("CV data cannot be null");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 72, 72, 72, 72); // 1 inch margins

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // Add content with actual data
            addHeader(document, cvData.personalInformation());
            addContactInfo(document, cvData.personalInformation());

            // Add summary if present
            if (cvData.summary() != null && !cvData.summary().isEmpty()) {
                addSummary(document, cvData.summary());
            }

            // Add work experience if present
            if (cvData.workExperience() != null && !cvData.workExperience().isEmpty()) {
                addWorkExperience(document, cvData.workExperience());
            }

            // Add education if present
            if (cvData.education() != null && !cvData.education().isEmpty()) {
                addEducation(document, cvData.education());
            }

            // Add projects if present
            if (cvData.projects() != null && !cvData.projects().isEmpty()) {
                addProjects(document, cvData.projects());
            }

            // Add skills if present
            if (cvData.skills() != null && !cvData.skills().isEmpty()) {
                addSkills(document, cvData.skills());
            }

            log.info("PDF content generated successfully");

        } catch (Exception e) {
            log.error("Error generating PDF", e);
            throw e;
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }

        byte[] pdfBytes = baos.toByteArray();
        log.info("PDF generated successfully. Size: {} bytes", pdfBytes.length);

        return pdfBytes;
    }

    private void addHeader(Document document, ApplierProfileCVPersonalInformationResponse personalInfo)
            throws DocumentException {
        String name = personalInfo != null && personalInfo.fullName() != null
                ? personalInfo.fullName()
                : "YOUR NAME";

        Paragraph title = new Paragraph(name, TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);
    }

    private void addContactInfo(Document document, ApplierProfileCVPersonalInformationResponse personalInfo)
            throws DocumentException {
        String email = personalInfo != null && personalInfo.email() != null
                ? personalInfo.email()
                : "email@example.com";

        Paragraph contact = new Paragraph("Email: " + email, SMALL_FONT);
        contact.setAlignment(Element.ALIGN_CENTER);
        contact.setSpacingAfter(20);
        document.add(contact);
    }

    private void addSummary(Document document, String summary) throws DocumentException {
        addSectionTitle(document, "PROFESSIONAL SUMMARY");

        Paragraph summaryPara = new Paragraph(summary, BODY_FONT);
        summaryPara.setSpacingAfter(15);
        document.add(summaryPara);
    }

    private void addWorkExperience(Document document,
                                   java.util.List<ApplierProfileCVWorkExperienceResponse> workExperiences)
            throws DocumentException {
        addSectionTitle(document, "WORK EXPERIENCE");

        for (ApplierProfileCVWorkExperienceResponse work : workExperiences) {
            addWorkExperienceEntry(document, work);
        }
    }

    private void addWorkExperienceEntry(Document document,
                                        ApplierProfileCVWorkExperienceResponse work) throws DocumentException {
        // Job title
        String title = work.title() != null ? work.title() : "Position";
        Paragraph jobTitle = new Paragraph(title, SUBTITLE_FONT);
        jobTitle.setSpacingAfter(5);
        document.add(jobTitle);

        // Company, dates, and location
        StringBuilder details = new StringBuilder();
        if (work.companyName() != null) {
            details.append(work.companyName());
        }
        if (work.startDate() != null || work.endDate() != null) {
            if (details.length() > 0) details.append(" | ");
            String startDate = work.startDate() != null ? work.startDate() : "Present";
            String endDate = work.endDate() != null ? work.endDate() : "Present";
            details.append(startDate).append(" - ").append(endDate);
        }
        if (work.location() != null) {
            if (details.length() > 0) details.append(" | ");
            details.append(work.location());
        }

        if (details.length() > 0) {
            Paragraph detailsPara = new Paragraph(details.toString(), SMALL_ITALIC);
            detailsPara.setSpacingAfter(8);
            document.add(detailsPara);
        }

        // Responsibilities as bullet points
        if (work.responsibilities() != null && !work.responsibilities().isEmpty()) {
            List list = new List(List.UNORDERED);
            list.setListSymbol("•");
            list.setIndentationLeft(20);

            for (String responsibility : work.responsibilities()) {
                ListItem item = new ListItem(responsibility, BODY_FONT);
                list.add(item);
            }

            document.add(list);
        }

        document.add(Chunk.NEWLINE);
    }

    private void addEducation(Document document,
                              java.util.List<ApplierProfileCVEducationResponse> educations)
            throws DocumentException {
        addSectionTitle(document, "EDUCATION");

        for (ApplierProfileCVEducationResponse edu : educations) {
            addEducationEntry(document, edu);
        }
    }

    private void addEducationEntry(Document document, ApplierProfileCVEducationResponse edu)
            throws DocumentException {
        // Degree and Major
        StringBuilder degreeInfo = new StringBuilder();
        if (edu.degree() != null) {
            degreeInfo.append(edu.degree());
        }
        if (edu.major() != null) {
            if (degreeInfo.length() > 0) degreeInfo.append(" in ");
            degreeInfo.append(edu.major());
        }

        if (degreeInfo.length() > 0) {
            Paragraph degree = new Paragraph(degreeInfo.toString(), SUBTITLE_FONT);
            degree.setSpacingAfter(5);
            document.add(degree);
        }

        // University and dates
        StringBuilder universityInfo = new StringBuilder();
        if (edu.university() != null) {
            universityInfo.append(edu.university());
        }
        if (edu.startDate() != null || edu.endDate() != null) {
            if (universityInfo.length() > 0) universityInfo.append(" | ");
            if (edu.startDate() != null) {
                universityInfo.append(edu.startDate());
            }
            if (edu.endDate() != null) {
                if (edu.startDate() != null) universityInfo.append(" - ");
                universityInfo.append(edu.endDate());
            }
        }
        if (edu.gpa() != null) {
            if (universityInfo.length() > 0) universityInfo.append(" | ");
            universityInfo.append("GPA: ").append(edu.gpa());
        }

        if (universityInfo.length() > 0) {
            Paragraph university = new Paragraph(universityInfo.toString(), SMALL_ITALIC);
            university.setSpacingAfter(15);
            document.add(university);
        }
    }

    private void addProjects(Document document,
                             java.util.List<ApplierProfileCVProjectResponse> projects)
            throws DocumentException {
        addSectionTitle(document, "PROJECTS");

        for (ApplierProfileCVProjectResponse project : projects) {
            addProjectEntry(document, project);
        }
    }

    private void addProjectEntry(Document document, ApplierProfileCVProjectResponse project)
            throws DocumentException {
        // Project name
        if (project.name() != null) {
            Paragraph projectName = new Paragraph(project.name(), SUBTITLE_FONT);
            projectName.setSpacingAfter(5);
            document.add(projectName);
        }

        // Project description
        if (project.description() != null && !project.description().isEmpty()) {
            Paragraph description = new Paragraph(project.description(), BODY_FONT);
            description.setSpacingAfter(5);
            document.add(description);
        }

        // Technologies
        if (project.technologies() != null && !project.technologies().isEmpty()) {
            Paragraph techLabel = new Paragraph("Technologies: ", SMALL_FONT);

            StringBuilder techList = new StringBuilder();
            for (int i = 0; i < project.technologies().size(); i++) {
                techList.append(project.technologies().get(i));
                if (i < project.technologies().size() - 1) {
                    techList.append(", ");
                }
            }

            Chunk techChunk = new Chunk(techList.toString(), SMALL_ITALIC);
            techLabel.add(techChunk);
            techLabel.setSpacingAfter(5);
            document.add(techLabel);
        }

        // Project link
        if (project.link() != null && !project.link().isEmpty()) {
            Paragraph link = new Paragraph("Link: " + project.link(), SMALL_ITALIC);
            link.setSpacingAfter(15);
            document.add(link);
        } else {
            document.add(new Paragraph(" "));
        }
    }

    private void addSkills(Document document,
                           java.util.List<ApplierProfileCVSkillResponse> skills)
            throws DocumentException {
        addSectionTitle(document, "SKILLS");

        // Group skills by level
        Map<String, java.util.List<String>> skillsByLevel = new HashMap<>();

        for (ApplierProfileCVSkillResponse skill : skills) {
            String level = skill.level() != null ? skill.level() : "Other";
            skillsByLevel.computeIfAbsent(level, k -> new java.util.ArrayList<>())
                    .add(skill.name());
        }

        // If we have 3 or fewer categories, use table layout
        if (skillsByLevel.size() <= 3) {
            PdfPTable table = new PdfPTable(Math.min(skillsByLevel.size(), 3));
            table.setWidthPercentage(100);
            table.setSpacingAfter(15);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            for (Map.Entry<String, java.util.List<String>> entry : skillsByLevel.entrySet()) {
                addSkillColumn(table, entry.getKey(), entry.getValue());
            }

            document.add(table);
        } else {
            // For many categories, use list format
            for (Map.Entry<String, java.util.List<String>> entry : skillsByLevel.entrySet()) {
                Paragraph levelHeader = new Paragraph(entry.getKey(), SUBTITLE_FONT);
                levelHeader.setSpacingAfter(5);
                document.add(levelHeader);

                List skillList = new List(List.UNORDERED);
                skillList.setListSymbol("•");
                skillList.setIndentationLeft(20);

                for (String skillName : entry.getValue()) {
                    ListItem item = new ListItem(skillName, BODY_FONT);
                    skillList.add(item);
                }

                document.add(skillList);
                document.add(new Paragraph(" "));
            }
        }
    }

    private void addSkillColumn(PdfPTable table, String category,
                                java.util.List<String> skillNames) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);

        // Category header
        Paragraph header = new Paragraph(category, SUBTITLE_FONT);
        header.setSpacingAfter(5);
        cell.addElement(header);

        // Skills list
        List skillList = new List(List.UNORDERED);
        skillList.setListSymbol("•");
        skillList.setIndentationLeft(15);

        for (String skillName : skillNames) {
            ListItem item = new ListItem(skillName, SMALL_FONT);
            skillList.add(item);
        }

        cell.addElement(skillList);
        table.addCell(cell);
    }

    private void addSectionTitle(Document document, String title) throws DocumentException {
        // Add spacing before section
        document.add(new Paragraph(" "));

        // Create section heading
        Paragraph heading = new Paragraph(title, SECTION_FONT);
        heading.setSpacingAfter(10);

        // Add bottom border
        LineSeparator line = new LineSeparator(1, 100, new BaseColor(44, 62, 80),
                Element.ALIGN_LEFT, -2);

        document.add(heading);
        document.add(new Chunk(line));
        document.add(new Paragraph(" "));
    }
}