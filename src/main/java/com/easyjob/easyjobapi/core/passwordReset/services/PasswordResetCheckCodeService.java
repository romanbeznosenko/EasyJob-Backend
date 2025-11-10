package com.easyjob.easyjobapi.core.passwordReset.services;//package com.easyjob.easyjobapi.core.passwordReset.services;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import com.easyjob.easyjobapi.core.login.management.AccountNotActivatedException;
//import com.easyjob.easyjobapi.core.passwordReset.management.*;
//import com.easyjob.easyjobapi.core.passwordReset.models.PasswordResetToken;
//import com.easyjob.easyjobapi.core.passwordReset.models.PasswordResetTokenCheckRequest;
//import com.easyjob.easyjobapi.core.passwordReset.models.PasswordResetTokenDAO;
//import com.easyjob.easyjobapi.core.passwordReset.models.PasswordResetTokenSendRequest;
//import com.easyjob.easyjobapi.core.user.management.UserNotFoundException;
//import com.easyjob.easyjobapi.utils.CycleAvoidingMappingContext;
//
//import java.time.Instant;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class PasswordResetCheckCodeService {
//    private final PasswordResetTokenMapper passwordResetTokenMapper;
//    private final PasswordResetTokenManager passwordResetTokenManager;
//    private final SendPasswordResetEmailService sendPasswordResetEmailService;
//
//    public void checkPasswordResetCode(PasswordResetTokenCheckRequest request) throws PasswordResetTokenNotFoundException, UserNotFoundException, PasswordResetTokenAlreadyGeneratedException, PasswordResetTokenExpiredException, AccountNotActivatedException, PasswordResetEmailSendingException {
//        log.info("Check if password reset token is correct!");
//        PasswordResetTokenDAO passwordResetTokenDAO = passwordResetTokenManager.findPasswordResetTokenByToken(
//                                                                                       request.code())
//                                                                               .orElseThrow(
//                                                                                       () -> new PasswordResetTokenNotFoundException(
//                                                                                               "Password reset token not found!"));
//        PasswordResetToken passwordResetToken = passwordResetTokenMapper.mapToDomain(passwordResetTokenDAO,
//                                                                                     new CycleAvoidingMappingContext());
//        if (Instant.now()
//                   .isAfter(passwordResetToken.getExpiryDate())) {
//            sendPasswordResetEmailService.sendPasswordResetEmail(PasswordResetTokenSendRequest.builder()
//                                                                                              .email(passwordResetToken.getAuthAccount()
//                                                                                                                       .getEmail())
//                                                                                              .build());
//            throw new PasswordResetTokenExpiredException(
//                    "Password reset token has expired! New was sent to with email!");
//        }
//        log.info("Checked if password reset token is correct!");
//    }
//}
//
