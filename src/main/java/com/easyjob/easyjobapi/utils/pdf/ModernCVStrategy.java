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
public class ModernCVStrategy extends AbstractCVGenerationStrategy {
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 28, Font.BOLD, DARK_BLUE);
    private static final Font SECTION_FONT = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, DARK_BLUE);
    private static final Font SUBTITLE_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, DARK_GRAY);
    private static final Font BODY_FONT = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
    private static final Font SMALL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, MEDIUM_GRAY);
    private static final Font SMALL_ITALIC = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, MEDIUM_GRAY);

    @Override
    public byte[] generatePDF(ApplierProfileCVResponse cvData) throws Exception {
        log.info("Generating Modern style CV");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = createDocument();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            addHeader(document, cvData.personalInformation());
            addContactInfo(document, cvData.personalInformation());

            if (cvData.summary() != null && !cvData.summary().isEmpty()) {
                addSummary(document, cvData.summary());
            }

            if (cvData.workExperience() != null && !cvData.workExperience().isEmpty()) {
                addWorkExperience(document, cvData.workExperience());
            }

            if (cvData.education() != null && !cvData.education().isEmpty()) {
                addEducation(document, cvData.education());
            }

            if (cvData.projects() != null && !cvData.projects().isEmpty()) {
                addProjects(document, cvData.projects());
            }

            if (cvData.skills() != null && !cvData.skills().isEmpty()) {
                addSkills(document, cvData.skills());
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
        return "MODERN";
    }

    private void addHeader(Document document, ApplierProfileCVPersonalInformationResponse personalInfo)
            throws DocumentException {
        String name = safeString(personalInfo != null ? personalInfo.fullName() : null, "YOUR NAME");
        Paragraph title = new Paragraph(name, TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);
    }

    private void addContactInfo(Document document, ApplierProfileCVPersonalInformationResponse personalInfo)
            throws DocumentException {
        String email = safeString(personalInfo != null ? personalInfo.email() : null, "email@example.com");
        Paragraph contact = new Paragraph("Email: " + email, SMALL_FONT);
        contact.setAlignment(Element.ALIGN_CENTER);
        contact.setSpacingAfter(20);
        document.add(contact);
    }

    private void addSummary(Document document, String summary) throws DocumentException {
        addSectionTitle(document, "PROFESSIONAL SUMMARY");
        Paragraph summaryPara = new Paragraph(summary, BODY_FONT);
        summaryPara.setSpacingAfter(15);
        summaryPara.setAlignment(Element.ALIGN_JUSTIFIED);
        document.add(summaryPara);
    }

    private void addWorkExperience(Document document, java.util.List<ApplierProfileCVWorkExperienceResponse> workExperiences)
            throws DocumentException {
        addSectionTitle(document, "WORK EXPERIENCE");

        for (ApplierProfileCVWorkExperienceResponse work : workExperiences) {
            Paragraph jobTitle = new Paragraph(safeString(work.title(), "Position"), SUBTITLE_FONT);
            jobTitle.setSpacingAfter(5);
            document.add(jobTitle);

            StringBuilder details = new StringBuilder();
            if (work.companyName() != null) details.append(work.companyName());
            if (work.startDate() != null || work.endDate() != null) {
                if (details.length() > 0) details.append(" | ");
                details.append(safeString(work.startDate(), "Present"))
                        .append(" - ")
                        .append(safeString(work.endDate(), "Present"));
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

            if (work.responsibilities() != null && !work.responsibilities().isEmpty()) {
                com.itextpdf.text.List list = createBulletList("•", 20);
                for (String resp : work.responsibilities()) {
                    list.add(new ListItem(resp, BODY_FONT));
                }
                document.add(list);
            }

            addSpacing(document, 10);
        }
    }

    private void addEducation(Document document, java.util.List<ApplierProfileCVEducationResponse> educations)
            throws DocumentException {
        addSectionTitle(document, "EDUCATION");

        for (ApplierProfileCVEducationResponse edu : educations) {
            StringBuilder degreeInfo = new StringBuilder();
            if (edu.degree() != null) degreeInfo.append(edu.degree());
            if (edu.major() != null) {
                if (degreeInfo.length() > 0) degreeInfo.append(" in ");
                degreeInfo.append(edu.major());
            }

            if (degreeInfo.length() > 0) {
                Paragraph degree = new Paragraph(degreeInfo.toString(), SUBTITLE_FONT);
                degree.setSpacingAfter(5);
                document.add(degree);
            }

            StringBuilder universityInfo = new StringBuilder();
            if (edu.university() != null) universityInfo.append(edu.university());
            if (edu.startDate() != null || edu.endDate() != null) {
                if (universityInfo.length() > 0) universityInfo.append(" | ");
                if (edu.startDate() != null) universityInfo.append(edu.startDate());
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
    }

    private void addProjects(Document document, java.util.List<ApplierProfileCVProjectResponse> projects)
            throws DocumentException {
        addSectionTitle(document, "PROJECTS");

        for (ApplierProfileCVProjectResponse project : projects) {
            if (project.name() != null) {
                Paragraph projectName = new Paragraph(project.name(), SUBTITLE_FONT);
                projectName.setSpacingAfter(5);
                document.add(projectName);
            }

            if (project.description() != null && !project.description().isEmpty()) {
                Paragraph description = new Paragraph(project.description(), BODY_FONT);
                description.setSpacingAfter(5);
                description.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(description);
            }

            if (project.technologies() != null && !project.technologies().isEmpty()) {
                String techList = String.join(", ", project.technologies());
                Paragraph tech = new Paragraph("Technologies: " + techList, SMALL_ITALIC);
                tech.setSpacingAfter(5);
                document.add(tech);
            }

            if (project.link() != null && !project.link().isEmpty()) {
                Paragraph link = new Paragraph("Link: " + project.link(), SMALL_ITALIC);
                link.setSpacingAfter(15);
                document.add(link);
            } else {
                addSpacing(document, 10);
            }
        }
    }

    private void addSkills(Document document, java.util.List<ApplierProfileCVSkillResponse> skills)
            throws DocumentException {
        addSectionTitle(document, "SKILLS");

        Map<String, java.util.List<String>> skillsByLevel = new HashMap<>();
        for (ApplierProfileCVSkillResponse skill : skills) {
            String level = safeString(skill.level(), "Other");
            skillsByLevel.computeIfAbsent(level, k -> new ArrayList<>()).add(skill.name());
        }

        if (skillsByLevel.size() <= 3) {
            PdfPTable table = createBorderlessTable(Math.min(skillsByLevel.size(), 3));
            table.setSpacingAfter(15);

            for (Map.Entry<String, java.util.List<String>> entry : skillsByLevel.entrySet()) {
                PdfPCell cell = createBorderlessCell();

                Paragraph header = new Paragraph(entry.getKey(), SUBTITLE_FONT);
                header.setSpacingAfter(5);
                cell.addElement(header);

                com.itextpdf.text.List skillList = createBulletList("•", 15);
                for (String skillName : entry.getValue()) {
                    skillList.add(new ListItem(skillName, SMALL_FONT));
                }

                cell.addElement(skillList);
                table.addCell(cell);
            }

            document.add(table);
        } else {
            for (Map.Entry<String, java.util.List<String>> entry : skillsByLevel.entrySet()) {
                Paragraph levelHeader = new Paragraph(entry.getKey(), SUBTITLE_FONT);
                levelHeader.setSpacingAfter(5);
                document.add(levelHeader);

                com.itextpdf.text.List skillList = createBulletList("•", 20);
                for (String skillName : entry.getValue()) {
                    skillList.add(new ListItem(skillName, BODY_FONT));
                }

                document.add(skillList);
                addSpacing(document, 5);
            }
        }
    }

    private void addSectionTitle(Document document, String title) throws DocumentException {
        addSpacing(document, 5);

        Paragraph heading = new Paragraph(title, SECTION_FONT);
        heading.setSpacingAfter(10);
        document.add(heading);

        document.add(new Chunk(createSectionLine(DARK_BLUE, 2)));
        addSpacing(document, 5);
    }
}