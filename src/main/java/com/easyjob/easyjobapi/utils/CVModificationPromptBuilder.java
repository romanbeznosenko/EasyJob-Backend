package com.easyjob.easyjobapi.utils;

import com.easyjob.easyjobapi.core.offer.models.OfferDAO;

public class CVModificationPromptBuilder {

    public static String createPrompt(OfferDAO offer) {
        return """
            You are an expert technical recruiter and resume optimization specialist.
            
            I have attached a CV/resume file. Your task is to:
            1. Parse and extract all information from the attached CV
            2. Analyze the job offer details provided below
            3. Optimize and tailor the CV content to align with the job requirements
            4. Output a structured JSON that can be used to generate an enhanced CV
            
            === JOB OFFER DETAILS ===
            Title: %s
            
            Description:
            %s
            
            Requirements:
            %s
            
            Responsibilities:
            %s
            
            === OPTIMIZATION GUIDELINES ===
            1. Enhance work experience descriptions:
               - Use strong action verbs (Developed, Designed, Implemented, Optimized, Led, Achieved)
               - Highlight accomplishments that match the job requirements
               - Quantify achievements where possible
               - Emphasize relevant technologies and skills from the job posting
            
            2. Optimize project descriptions:
               - Focus on projects that demonstrate skills required for this position
               - Highlight relevant technologies mentioned in the job requirements
               - Add technical depth where appropriate
            
            3. Tailor skills section:
               - Prioritize skills that match job requirements
               - Include proficiency levels
               - Group related skills logically
            
            4. Create a compelling summary:
               - 2-3 sentences highlighting the candidate's fit for THIS specific role
               - Mention key qualifications that align with job requirements
               - Emphasize relevant experience and achievements
            
            5. Maintain accuracy:
               - Keep all factual information (dates, company names, education details) unchanged
               - Do not fabricate experience or skills
               - Only enhance the presentation of existing information
            
            === OUTPUT FORMAT ===
            
            Respond ONLY with valid JSON (no markdown code blocks, no additional text):
            
            {
              "personal_information": {
                "full_name": "extracted from CV",
                "email": "extracted from CV",
                "phone": "extracted from CV if available",
                "location": "extracted from CV if available",
                "linkedin": "extracted from CV if available"
              },
              "summary": "2-3 sentence professional summary tailored to this job offer",
              "education": [
                {
                  "degree": "",
                  "university": "",
                  "major": "",
                  "start_date": "YYYY-MM",
                  "end_date": "YYYY-MM or null if current",
                  "gpa": "X.XX or null"
                }
              ],
              "work_experience": [
                {
                  "title": "",
                  "company_name": "",
                  "start_date": "YYYY-MM",
                  "end_date": "YYYY-MM or null if current",
                  "location": "",
                  "responsibilities": [
                    "Enhanced responsibility description with action verbs and quantified achievements"
                  ]
                }
              ],
              "projects": [
                {
                  "name": "",
                  "description": "Enhanced description highlighting relevance to job requirements",
                  "technologies": ["tech1", "tech2"],
                  "link": "url or null"
                }
              ],
              "skills": [
                {
                  "name": "skill name",
                  "level": "Beginner/Intermediate/Advanced/Expert",
                  "category": "Programming/Framework/Tool/Soft Skill"
                }
              ],
              "certifications": [
                {
                  "name": "",
                  "issuer": "",
                  "date": "YYYY-MM",
                  "url": "url or null"
                }
              ],
              "languages": [
                {
                  "name": "",
                  "proficiency": "Native/Fluent/Professional/Conversational/Basic"
                }
              ]
            }
            
            Extract all information from the attached CV and optimize it for this specific job opportunity.
            Be professional, accurate, and ensure all enhancements are truthful representations of the candidate's background.
            """.formatted(
                offer.getName(),
                offer.getDescription() != null ? offer.getDescription() : "Not specified",
                offer.getRequirements() != null ? offer.getRequirements() : "Not specified",
                offer.getResponsibilities() != null ? offer.getResponsibilities() : "Not specified"
        );
    }
}