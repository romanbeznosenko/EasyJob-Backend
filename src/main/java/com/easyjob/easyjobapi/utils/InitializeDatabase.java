package com.easyjob.easyjobapi.utils;

import com.easyjob.easyjobapi.core.authAccount.management.AuthAccountManager;
import com.easyjob.easyjobapi.core.authAccount.models.AuthAccountDAO;
import com.easyjob.easyjobapi.core.firm.management.FirmManager;
import com.easyjob.easyjobapi.core.firm.models.FirmDAO;
import com.easyjob.easyjobapi.core.offer.magenement.OfferManager;
import com.easyjob.easyjobapi.core.offer.models.OfferDAO;
import com.easyjob.easyjobapi.core.user.management.UserManager;
import com.easyjob.easyjobapi.core.user.models.UserDAO;
import com.easyjob.easyjobapi.utils.enums.AuthTypeEnum;
import com.easyjob.easyjobapi.utils.enums.UserTypeEnum;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InitializeDatabase {
    private final UserManager userManager;
    private final PasswordEncoder passwordEncoder;
    private final AuthAccountManager authAccountManager;
    private final FirmManager firmManager;
    private final OfferManager offerManager;

    @PostConstruct
    public void initializeDatabase() {
        UserDAO userDAO = userManager.findUserByEmailAndArchivedFalse("recruiter@easyjob.local")
                .orElse(null);

        if (userDAO == null) {
            userDAO = createAdminUser();
            FirmDAO firmDAO = createFirm(userDAO);
            createOffers(firmDAO);
        }
    }

    private UserDAO createAdminUser() {
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
}
