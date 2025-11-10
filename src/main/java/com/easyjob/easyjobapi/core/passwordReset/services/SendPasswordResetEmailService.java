package com.easyjob.easyjobapi.core.passwordReset.services;//package com.easyjob.easyjobapi.core.passwordReset.services;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.RandomStringUtils;
//import org.springframework.stereotype.Service;
//import com.easyjob.easyjobapi.core.authAccount.management.AuthAccountManager;
//import com.easyjob.easyjobapi.core.authAccount.management.AuthAccountMapper;
//import com.easyjob.easyjobapi.core.authAccount.models.AuthAccount;
//import com.easyjob.easyjobapi.core.authAccount.models.AuthAccountDAO;
//import com.easyjob.easyjobapi.core.login.management.AccountNotActivatedException;
//import com.easyjob.easyjobapi.core.passwordReset.management.PasswordResetEmailSendingException;
//import com.easyjob.easyjobapi.core.passwordReset.management.PasswordResetTokenAlreadyGeneratedException;
//import com.easyjob.easyjobapi.core.passwordReset.management.PasswordResetTokenManager;
//import com.easyjob.easyjobapi.core.passwordReset.management.PasswordResetTokenMapper;
//import com.easyjob.easyjobapi.core.passwordReset.models.PasswordResetToken;
//import com.easyjob.easyjobapi.core.passwordReset.models.PasswordResetTokenDAO;
//import com.easyjob.easyjobapi.core.passwordReset.models.PasswordResetTokenSendRequest;
//import com.easyjob.easyjobapi.core.user.management.UserNotFoundException;
//import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
//import com.easyjob.easyjobapi.utils.mailer.PasswordResetEmailService;
//
//import java.time.Duration;
//import java.time.Instant;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class SendPasswordResetEmailService {
//    private final PasswordResetTokenManager passwordResetTokenManager;
//    private final PasswordResetTokenMapper passwordResetTokenMapper;
//    private final AuthAccountManager authAccountManager;
//    private final PasswordResetEmailService passwordResetEmailService;
//    private final AuthAccountMapper authAccountMapper;
//
//    public void sendPasswordResetEmail(PasswordResetTokenSendRequest request) throws UserNotFoundException, PasswordResetTokenAlreadyGeneratedException, AccountNotActivatedException, PasswordResetEmailSendingException {
//        log.info("Sending password reset email");
//        AuthAccountDAO authAccountDAO = authAccountManager.findByEmail(request.email()
//                                                                              .toLowerCase())
//                                                          .orElseThrow(() -> new UserNotFoundException(
//                                                                  "Account with given email does not exist!"));
//        AuthAccount authAccount = authAccountMapper.mapToDomain(authAccountDAO, new CycleAvoidingMappingContext());
//        if (!authAccount.getIsActivated()) throw new AccountNotActivatedException("Account is not activated!");
//        PasswordResetTokenDAO passwordResetTokenDAO = passwordResetTokenManager.findPasswordResetTokenByUser(
//                                                                                       authAccountDAO)
//                                                                               .orElse(null);
//        if (passwordResetTokenDAO != null) {
//            PasswordResetToken passwordResetToken = passwordResetTokenMapper.mapToDomain(passwordResetTokenDAO,
//                                                                                         new CycleAvoidingMappingContext());
//            long duration = Duration.between(passwordResetToken.getCreatedAt(), Instant.now())
//                                    .toMinutes();
//            if (duration < 3)
//                throw new PasswordResetTokenAlreadyGeneratedException(
//                        "Password reset token has been generated less than 3 minutes ago! Try again in " + (3 - duration) + " minutes");
//            passwordResetTokenManager.deletePasswordResetToken(passwordResetTokenDAO);
//        }
//        String token = RandomStringUtils.random(4, "0123456789");
//        PasswordResetToken passwordResetToken = PasswordResetTokenBuilders.buildPasswordResetToken(authAccount, token);
//        passwordResetEmailService.sendPasswordRecoveryEmail(authAccount, passwordResetToken.getToken());
//        passwordResetTokenDAO = passwordResetTokenMapper.mapToEntity(passwordResetToken,
//                                                                     new CycleAvoidingMappingContext());
//        passwordResetTokenManager.savePasswordResetTokenToDatabase(passwordResetTokenDAO);
//        log.info("Sent password reset  email to user: {}", authAccount.getEmail());
//    }
//}
//
