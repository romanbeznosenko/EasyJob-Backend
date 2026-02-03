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
            
            === CRITICAL RULES - MUST FOLLOW ===
            
            **ACCURACY AND HONESTY:**
            - NEVER fabricate, invent, or add any information not present in the original CV
            - NEVER add skills, technologies, or experiences that are not explicitly mentioned
            - NEVER invent project details, achievements, or responsibilities
            - NEVER add certifications, languages, or qualifications not in the CV
            - Keep all dates, company names, job titles, and educational details EXACTLY as they appear
            - If information is missing from the CV, leave those fields empty or null - DO NOT guess or infer
            
            **WHAT YOU CAN DO:**
            - Rephrase existing responsibilities using stronger action verbs (Developed, Designed, Implemented, Led, Achieved)
            - Reorganize existing content to emphasize relevance to the job posting
            - Highlight existing skills and experiences that match job requirements
            - Improve grammar, formatting, and professional tone of existing content
            - Reorder sections to prioritize most relevant information
            - Combine or split existing bullet points for better clarity
            
            **WHAT YOU CANNOT DO:**
            - Add new skills not mentioned in the CV
            - Inflate experience duration or responsibilities
            - Add technologies or tools not mentioned
            - Create fictional projects or achievements
            - Add proficiency levels not indicated in the CV
            - Invent metrics or numbers not in the original
            
            === OPTIMIZATION GUIDELINES ===
            
            1. **Extract Information Accurately:**
               - Read the CV carefully and extract ALL information exactly as written
               - If a field is not present in the CV, set it to null or omit it
               - Preserve all factual details without modification
            
            2. **Enhance Work Experience Descriptions:**
               - Use the EXISTING responsibilities and achievements
               - Rephrase with strong action verbs (only if the meaning stays the same)
               - Emphasize aspects that align with job requirements
               - DO NOT add responsibilities that aren't in the original CV
            
            3. **Optimize Project Descriptions:**
               - Use ONLY projects mentioned in the CV
               - Enhance descriptions using information already present
               - Highlight technologies that match job requirements (only if already mentioned)
               - DO NOT add technical details not in the original
            
            4. **Organize Skills Section:**
               - List ONLY skills explicitly mentioned in the CV
               - Use proficiency levels ONLY if stated in the CV, otherwise omit
               - Prioritize skills matching job requirements in the order
               - DO NOT add related or assumed skills
            
            5. **Create Professional Summary:**
               - Base summary ONLY on information present in the CV
               - Highlight qualifications that align with THIS job offer
               - 2-3 sentences maximum
               - DO NOT claim skills or experience not in the CV
            
            6. **Personal Information:**
               - Extract exactly as written in the CV
               - Include only fields that are present (name, email, phone, location, linkedin)
               - DO NOT add placeholder or assumed contact information
            
            === OUTPUT FORMAT ===
            
            Respond ONLY with valid JSON (no markdown code blocks, no additional text):
            
            {
              "personal_information": {
                "full_name": "extracted from CV or null",
                "email": "extracted from CV or null",
                "phone": "extracted from CV or null",
                "location": "extracted from CV or null",
                "linkedin": "extracted from CV or null"
              },
              "summary": "2-3 sentence professional summary based ONLY on CV content, tailored to job offer",
              "education": [
                {
                  "degree": "exactly from CV",
                  "university": "exactly from CV",
                  "major": "exactly from CV",
                  "start_date": "YYYY-MM from CV or null",
                  "end_date": "YYYY-MM from CV or null",
                  "gpa": "X.XX from CV or null"
                }
              ],
              "work_experience": [
                {
                  "title": "exactly from CV",
                  "company_name": "exactly from CV",
                  "start_date": "YYYY-MM from CV or null",
                  "end_date": "YYYY-MM from CV or null",
                  "location": "exactly from CV or null",
                  "responsibilities": [
                    "Enhanced version of EXISTING responsibility - do not add new ones"
                  ]
                }
              ],
              "projects": [
                {
                  "name": "exactly from CV",
                  "description": "Enhanced description using ONLY information from CV",
                  "technologies": ["only technologies mentioned in CV"],
                  "link": "url from CV or null"
                }
              ],
              "skills": [
                {
                  "name": "skill name from CV only",
                  "level": "level from CV or null - do not assume",
                  "category": "category if clear from context or null"
                }
              ],
              "certifications": [
                {
                  "name": "only if present in CV",
                  "issuer": "only if present in CV",
                  "date": "YYYY-MM only if present in CV",
                  "url": "url from CV or null"
                }
              ],
              "languages": [
                {
                  "name": "only languages mentioned in CV",
                  "proficiency": "only if explicitly stated in CV, otherwise null"
                }
              ]
            }
            
            **REMINDER:** Your role is to REORGANIZE and REPHRASE existing content, NOT to add new information.
            Only work with what is explicitly present in the attached CV file.
            When in doubt, err on the side of accuracy over enhancement.
            Missing information should be represented as null or empty arrays - never invented.
            """.formatted(
                offer.getName(),
                offer.getDescription() != null ? offer.getDescription() : "Not specified",
                offer.getRequirements() != null ? offer.getRequirements() : "Not specified",
                offer.getResponsibilities() != null ? offer.getResponsibilities() : "Not specified"
        );
    }
}