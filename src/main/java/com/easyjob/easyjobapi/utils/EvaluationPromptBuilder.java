package com.easyjob.easyjobapi.utils;

import com.easyjob.easyjobapi.core.offer.models.OfferDAO;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models.EducationDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.ProjectDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.models.SkillDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.models.WorkExperienceDAO;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

public class EvaluationPromptBuilder {
    public static String createPrompt(
            OfferDAO offer,
            ApplierProfileDAO applierProfile,
            List<SkillDAO> skills,
            List<WorkExperienceDAO> workExperiences,
            List<EducationDAO> educations,
            List<ProjectDAO> projects
    ) {
        String skillsSection = formatSkills(skills);
        String experienceSection = formatWorkExperience(workExperiences);
        String educationSection = formatEducation(educations);
        String projectsSection = formatProjects(projects);

        return """
            You are an expert HR recruiter and technical hiring manager. Evaluate the following candidate for the job position with detailed analysis.
            
            === JOB POSITION ===
            Title: %s
            
            Description:
            %s
            
            Requirements:
            %s
            
            Responsibilities:
            %s
            
            === CANDIDATE PROFILE ===
            
            Skills:
            %s
            
            Work Experience:
            %s
            
            Education:
            %s
            
            Projects:
            %s
            
            === EVALUATION TASK ===
            
            Provide a comprehensive evaluation with:
            
            1. **Overall Match Score** (0-100): Single numeric score for overall fit
            
            2. **Skills Analysis** (0-100):
               - List required skills from job posting
               - Identify candidate's matching skills
               - Identify missing skills
               - Note transferable skills
            
            3. **Experience Evaluation** (0-100):
               - Assess if candidate meets experience requirements
               - Evaluate relevance of past roles
               - Consider career progression
            
            4. **Education Assessment** (0-100):
               - Check educational background vs requirements
               - Consider field of study relevance
            
            5. **Projects Relevance** (0-100):
               - Evaluate project alignment with responsibilities
               - Assess technical complexity
            
            6. **Key Strengths** (3-5 bullet points):
               - Specific advantages this candidate brings
               - Areas exceeding requirements
            
            7. **Key Weaknesses/Gaps** (3-5 bullet points):
               - Missing skills or experience
               - Areas for development
            
            8. **Cultural and Soft Skills Indicators**:
               - Communication style from profile
               - Collaboration indicators
            
            9. **Growth Potential**:
               - Learning trajectory
               - Ability to grow into role
            
            10. **Hiring Recommendation**:
                - STRONG_MATCH (80-100)
                - GOOD_MATCH (60-79)
                - MODERATE_MATCH (40-59)
                - WEAK_MATCH (0-39)
            
            11. **Interview Focus Areas** (3-5 topics)
            
            12. **Detailed Summary** (2-3 paragraphs)
            
            === OUTPUT FORMAT ===
            
            Respond ONLY with valid JSON:
            
            {
              "overallScore": <number 0-100>,
              "skillsScore": <number 0-100>,
              "experienceScore": <number 0-100>,
              "educationScore": <number 0-100>,
              "projectsScore": <number 0-100>,
              "skillsAnalysis": {
                "requiredSkills": ["skill1", "skill2"],
                "candidateHas": ["skill1", "skill3"],
                "missingSkills": ["skill2"],
                "transferableSkills": ["skill4"]
              },
              "strengths": [
                "First strength with details",
                "Second strength with details",
                "Third strength with details"
              ],
              "weaknesses": [
                "First gap with details",
                "Second gap with details"
              ],
              "culturalFit": "Assessment of soft skills",
              "growthPotential": "Assessment of learning ability",
              "recommendation": "STRONG_MATCH",
              "interviewFocusAreas": [
                "Topic 1",
                "Topic 2",
                "Topic 3"
              ],
              "detailedSummary": "Comprehensive evaluation summary"
            }
            
            Be objective, fair, and base scores on concrete evidence.
            """.formatted(
                offer.getName(),
                offer.getDescription() != null ? offer.getDescription() : "Not specified",
                offer.getRequirements() != null ? offer.getRequirements() : "Not specified",
                offer.getResponsibilities() != null ? offer.getResponsibilities() : "Not specified",
                skillsSection,
                experienceSection,
                educationSection,
                projectsSection
        );
    }


    private static String formatSkills(List<SkillDAO> skills) {
        if (skills == null || skills.isEmpty()) {
            return "No skills provided";
        }

        return skills.stream()
                .map(skill -> "- " + skill.getName() +
                        (skill.getLevel() != null ? " (" + skill.getLevel() + ")" : ""))
                .collect(Collectors.joining("\n"));
    }

    private static String formatWorkExperience(List<WorkExperienceDAO> experiences) {
        if (experiences == null || experiences.isEmpty()) {
            return "No work experience provided";
        }

        return experiences.stream()
                .map(exp -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("• ").append(exp.getTitle());

                    if (exp.getCompanyName() != null) {
                        sb.append(" at ").append(exp.getCompanyName());
                    }

                    if (exp.getLocation() != null) {
                        sb.append(" (").append(exp.getLocation()).append(")");
                    }

                    sb.append("\n  ");

                    if (exp.getStartDate() != null) {
                        sb.append(exp.getStartDate());
                        sb.append(" - ");
                        sb.append(exp.getEndDate() != null ? exp.getEndDate().toString() : "Present");

                        // Calculate duration
                        LocalDate end = exp.getEndDate() != null ? exp.getEndDate() : LocalDate.now();
                        Period period = Period.between(exp.getStartDate(), end);
                        int years = period.getYears();
                        int months = period.getMonths();

                        if (years > 0 || months > 0) {
                            sb.append(" (");
                            if (years > 0) sb.append(years).append(" year").append(years > 1 ? "s" : "");
                            if (years > 0 && months > 0) sb.append(", ");
                            if (months > 0) sb.append(months).append(" month").append(months > 1 ? "s" : "");
                            sb.append(")");
                        }
                    }

                    if (exp.getResponsibilities() != null && !exp.getResponsibilities().isEmpty()) {
                        sb.append("\n  Responsibilities: ").append(exp.getResponsibilities());
                    }

                    return sb.toString();
                })
                .collect(Collectors.joining("\n\n"));
    }

    private static String formatEducation(List<EducationDAO> educations) {
        if (educations == null || educations.isEmpty()) {
            return "No education provided";
        }

        return educations.stream()
                .map(edu -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("• ").append(edu.getDegree() != null ? edu.getDegree() : "Degree");

                    if (edu.getMajor() != null) {
                        sb.append(" in ").append(edu.getMajor());
                    }

                    if (edu.getUniversity() != null) {
                        sb.append("\n  ").append(edu.getUniversity());
                    }

                    if (edu.getStartDate() != null) {
                        sb.append("\n  ");
                        sb.append(edu.getStartDate().getYear());
                        if (edu.getEndDate() != null) {
                            sb.append(" - ").append(edu.getEndDate().getYear());
                        } else {
                            sb.append(" - Present");
                        }
                    }

                    if (edu.getGpa() != null) {
                        sb.append("\n  GPA: ").append(String.format("%.2f", edu.getGpa()));
                    }

                    return sb.toString();
                })
                .collect(Collectors.joining("\n\n"));
    }

    private static String formatProjects(List<ProjectDAO> projects) {
        if (projects == null || projects.isEmpty()) {
            return "No projects provided";
        }

        return projects.stream()
                .map(project -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("• ").append(project.getName());

                    if (project.getDescription() != null) {
                        sb.append("\n  Description: ").append(project.getDescription());
                    }

                    if (project.getTechnologies() != null) {
                        sb.append("\n  Technologies: ").append(project.getTechnologies());
                    }

                    if (project.getLink() != null) {
                        sb.append("\n  Link: ").append(project.getLink());
                    }

                    return sb.toString();
                })
                .collect(Collectors.joining("\n\n"));
    }
}
