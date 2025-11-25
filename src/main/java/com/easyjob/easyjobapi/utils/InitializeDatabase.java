package com.easyjob.easyjobapi.utils;

import com.easyjob.easyjobapi.core.authAccount.management.AuthAccountManager;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccountDAO;
import com.easyjob.easyjobapi.core.firm.management.FirmManager;
import com.easyjob.easyjobapi.core.firm.models.FirmDAO;
import com.easyjob.easyjobapi.core.offer.magenement.OfferManager;
import com.easyjob.easyjobapi.core.offer.models.OfferDAO;
import com.easyjob.easyjobapi.core.user.management.UserManager;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.modules.applierProfile.management.ApplierProfileManager;
import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.management.EducationManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models.EducationDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.management.ProjectManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.project.models.ProjectDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.management.SkillManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.skill.models.SkillDAO;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.management.WorkExperienceManager;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.workExperience.models.WorkExperienceDAO;
import com.easyjob.easyjobapi.utils.enums.AuthTypeEnum;
import com.easyjob.easyjobapi.utils.enums.UserTypeEnum;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class InitializeDatabase {
    private final UserManager userManager;
    private final PasswordEncoder passwordEncoder;
    private final AuthAccountManager authAccountManager;
    private final FirmManager firmManager;
    private final OfferManager offerManager;
    private final ApplierProfileManager applierProfileManager;
    private final EducationManager educationManager;
    private final ProjectManager projectManager;
    private final SkillManager skillManager;
    private final WorkExperienceManager workExperienceManager;

    @PostConstruct
    public void initializeDatabase() {
        UserDAO userDAO = userManager.findUserByEmailAndArchivedFalse("recruiter@easyjob.local")
                .orElse(null);

        if (userDAO == null) {
            log.info("Creating Recruiter User");
            userDAO = createRecruiterUser();
            FirmDAO firmDAO = createFirm(userDAO);
            createOffers(firmDAO);
        }

        UserDAO userDAO2 = userManager.findUserByEmailAndArchivedFalse("applier@easyjob.local")
                .orElse(null);

        if (userDAO2 == null) {
            log.info("Creating Applier User");
            userDAO2 = createApplierUser();
            ApplierProfileDAO applierProfileDAO = createApplierProfile(userDAO2);
            createEducation(applierProfileDAO);
            createProjects(applierProfileDAO);
            createSkills(applierProfileDAO);
            createWorkExperience(applierProfileDAO);
        }
    }

    private UserDAO createRecruiterUser() {
        UserDAO user = UserDAO.builder()
                .isArchived(false)
                .name("Recruiter")
                .email("recruiter@easyjob.local")
                .userType(UserTypeEnum.RECRUITER)
                .blocked(false)
                .surname("Pudzianowski")
                .build();

        user = userManager.saveUserToDatabase(user);

        AuthAccountDAO authAccount = AuthAccountDAO.builder()
                .authType(AuthTypeEnum.EMAIL)
                .email("recruiter@easyjob.local")
                .password(passwordEncoder.encode("recruiter"))
                .userId(user.getId())
                .isActivated(true)
                .build();

        authAccountManager.saveAuthAccountToDatabase(authAccount);

        return user;
    }

    private FirmDAO createFirm(UserDAO user) {
        FirmDAO firmDAO = FirmDAO.builder()
                .name("Hexagon")
                .description("Comarch is a Polish multinational software house and systems integrator based in Kraków, Poland.")
                .user(user)
                .location("Kraków, Poland")
                .logo(null)
                .isArchived(false)
                .build();

        return firmManager.saveToDatabase(firmDAO);
    }

    private void createOffers(FirmDAO firm) {
        OfferDAO offer = OfferDAO.builder()
                .name("Backend Developer")
                .description("We are looking for a Backend Developer experienced with Java, Spring Boot, and REST APIs to join our platform engineering team.")
                .firm(firm)
                .isArchived(false)
                .build();

        offerManager.saveToDatabase(offer);

        OfferDAO offer2 = OfferDAO.builder()
                .name("Junior QA Engineer")
                .description("Entry-level QA position focusing on manual testing, test case creation, and basic automation using Selenium.")
                .firm(firm)
                .isArchived(false)
                .build();

        offerManager.saveToDatabase(offer2);

        OfferDAO offer3 = OfferDAO.builder()
                .name("Full-Stack Developer")
                .description("A developer capable of working with React on the frontend and Node.js on the backend. Experience with CI/CD pipelines is a plus.")
                .firm(firm)
                .isArchived(false)
                .build();

        offerManager.saveToDatabase(offer3);
    }

    private UserDAO createApplierUser(){
        UserDAO user = UserDAO.builder()
                .isArchived(false)
                .name("Applier")
                .email("applier@easyjob.local")
                .userType(UserTypeEnum.APPLIER)
                .blocked(false)
                .surname("Pudzianowski")
                .build();

        user = userManager.saveUserToDatabase(user);

        AuthAccountDAO authAccount = AuthAccountDAO.builder()
                .authType(AuthTypeEnum.EMAIL)
                .email("applier@easyjob.local")
                .password(passwordEncoder.encode("applier"))
                .userId(user.getId())
                .isActivated(true)
                .build();

        authAccountManager.saveAuthAccountToDatabase(authAccount);

        return user;
    }

    private ApplierProfileDAO createApplierProfile(UserDAO user) {
        ApplierProfileDAO applierProfileDAO = ApplierProfileDAO.builder()
                .user(user)
                .cv(null)
                .isArchived(false)
                .build();

        return applierProfileManager.saveToDatabase(applierProfileDAO);
    }

    private void createEducation(ApplierProfileDAO applierProfile) {
        EducationDAO educationDAO = EducationDAO.builder()
                .applierProfile(applierProfile)
                .degree("Bachelor")
                .university("MIT")
                .startDate(LocalDate.of(2016, 9, 1))
                .endDate(LocalDate.of(2020, 6, 15))
                .major("Computer Science")
                .gpa(4.2)
                .build();

        educationManager.saveToDatabase(educationDAO);
    }

    private void createProjects(ApplierProfileDAO applierProfile) {
        ProjectDAO project1 = ProjectDAO.builder()
                .applierProfile(applierProfile)
                .name("Job Finder Platform")
                .description("A full-stack web application that allows users to search, filter, and apply for jobs. Includes authentication, saved jobs, and admin panel.")
                .technologies("Java, Spring Boot, React, PostgreSQL")
                .link("https://github.com/example/job-finder")
                .isArchived(false)
                .build();

        projectManager.saveToDatabase(project1);

        ProjectDAO project2 = ProjectDAO.builder()
                .applierProfile(applierProfile)
                .name("Android Calculator")
                .description("A mobile calculator application built with Android Studio featuring basic and advanced modes.")
                .technologies("Java, Android Studio, XML")
                .link("https://github.com/example/android-calculator")
                .isArchived(false)
                .build();

        projectManager.saveToDatabase(project2);

        ProjectDAO project3 = ProjectDAO.builder()
                .applierProfile(applierProfile)
                .name("Tire Price Tracking System")
                .description("A client-server application that scrapes tire prices, tracks price history, and displays changes for various brands.")
                .technologies("Python, FastAPI, Playwright, MySQL, PyQt6")
                .link("https://github.com/example/tire-price-tracker")
                .isArchived(false)
                .build();

        projectManager.saveToDatabase(project3);

        ProjectDAO project4 = ProjectDAO.builder()
                .applierProfile(applierProfile)
                .name("Parking Monitoring AI System")
                .description("A computer vision solution analyzing two synchronized video feeds to track cars, read license plates, and map vehicles to parking slots.")
                .technologies("Python, OpenCV, YOLO, SQLite, Multithreading")
                .link("https://github.com/example/parking-ai-system")
                .isArchived(false)
                .build();

        projectManager.saveToDatabase(project4);
    }

    private void createSkills(ApplierProfileDAO applierProfile) {
        SkillDAO skill1 = SkillDAO.builder()
                .applierProfile(applierProfile)
                .name("Java")
                .level("Advanced")
                .isArchived(false)
                .build();

        skillManager.saveToDatabase(skill1);

        SkillDAO skill2 = SkillDAO.builder()
                .applierProfile(applierProfile)
                .name("Spring Boot")
                .level("Intermediate")
                .isArchived(false)
                .build();

        skillManager.saveToDatabase(skill2);

        SkillDAO skill3 = SkillDAO.builder()
                .applierProfile(applierProfile)
                .name("React.js")
                .level("Intermediate")
                .isArchived(false)
                .build();

        skillManager.saveToDatabase(skill3);

        SkillDAO skill4 = SkillDAO.builder()
                .applierProfile(applierProfile)
                .name("SQL")
                .level("Advanced")
                .isArchived(false)
                .build();

        skillManager.saveToDatabase(skill4);

        SkillDAO skill5 = SkillDAO.builder()
                .applierProfile(applierProfile)
                .name("Docker")
                .level("Intermediate")
                .isArchived(false)
                .build();

        skillManager.saveToDatabase(skill5);

        SkillDAO skill6 = SkillDAO.builder()
                .applierProfile(applierProfile)
                .name("Python")
                .level("Intermediate")
                .isArchived(false)
                .build();

        skillManager.saveToDatabase(skill6);

        SkillDAO skill7 = SkillDAO.builder()
                .applierProfile(applierProfile)
                .name("Git")
                .level("Advanced")
                .isArchived(false)
                .build();

        skillManager.saveToDatabase(skill7);
    }

    private void createWorkExperience(ApplierProfileDAO applierProfile) {
        WorkExperienceDAO work1 = WorkExperienceDAO.builder()
                .applierProfile(applierProfile)
                .title("Backend Developer")
                .companyName("TechCorp Solutions")
                .startDate(LocalDate.of(2021, 3, 1))
                .endDate(LocalDate.of(2023, 1, 15))
                .responsibilities("Developed REST APIs using Spring Boot, optimized database queries, collaborated with frontend team, implemented CI/CD pipelines.")
                .location("Warsaw, Poland")
                .isArchived(false)
                .build();

        workExperienceManager.saveToDatabase(work1);

        WorkExperienceDAO work2 = WorkExperienceDAO.builder()
                .applierProfile(applierProfile)
                .title("Junior Software Engineer")
                .companyName("InnovateX")
                .startDate(LocalDate.of(2023, 2, 1))
                .endDate(null)
                .responsibilities("Implemented features in a microservices architecture, wrote unit/integration tests, maintained internal tools, participated in agile ceremonies.")
                .location("Remote")
                .isArchived(false)
                .build();

        workExperienceManager.saveToDatabase(work2);
    }
}
