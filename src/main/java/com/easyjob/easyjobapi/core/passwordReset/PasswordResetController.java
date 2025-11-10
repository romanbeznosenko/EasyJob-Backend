package com.easyjob.easyjobapi.core.passwordReset;//package com.easyjob.easyjobapi.core.passwordReset;
//
//import io.swagger.v3.oas.annotations.Operation;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//import com.easyjob.easyjobapi.core.login.management.AccountNotActivatedException;
//import com.easyjob.easyjobapi.core.passwordReset.management.*;
//import com.easyjob.easyjobapi.core.passwordReset.models.PasswordResetRequest;
//import com.easyjob.easyjobapi.core.passwordReset.models.PasswordResetTokenCheckRequest;
//import com.easyjob.easyjobapi.core.passwordReset.models.PasswordResetTokenSendRequest;
//import com.easyjob.easyjobapi.core.passwordReset.services.PasswordResetCheckCodeService;
//import com.easyjob.easyjobapi.core.passwordReset.services.PasswordResetService;
//import com.easyjob.easyjobapi.core.passwordReset.services.SendPasswordResetEmailService;
//import com.easyjob.easyjobapi.core.user.management.UserNotFoundException;
//import com.easyjob.easyjobapi.utils.CustomResponse;
//
//@RestController
//@RequiredArgsConstructor
//public class PasswordResetController {
//    private final PasswordResetService passwordResetService;
//    private final PasswordResetCheckCodeService passwordResetCheckCodeService;
//    private final SendPasswordResetEmailService sendPasswordResetEmailService;
//
//    @PostMapping(value = "/auth/password-reset")
//    @PreAuthorize("permitAll()")
//    @Operation(
//            summary = "Request password reset",
//            description = "Initiate password reset process by user")
//    public ResponseEntity<CustomResponse<String>> requestPasswordReset(
//            @Valid
//            @RequestBody PasswordResetTokenSendRequest request) throws UserNotFoundException, PasswordResetTokenAlreadyGeneratedException, AccountNotActivatedException, PasswordResetEmailSendingException {
//        sendPasswordResetEmailService.sendPasswordResetEmail(request);
//        return new ResponseEntity<>(new CustomResponse<>(null, "Password reset email sent.", HttpStatus.OK),
//                                    HttpStatus.OK);
//    }
//
//    @PostMapping(value = "/auth/password-reset/reset")
//    @PreAuthorize("permitAll()")
//    @Operation(
//            summary = "Reset password",
//            description = "Finish password reset byh providing code and new password")
//    public ResponseEntity<CustomResponse<String>> resetUserPassword(
//            @Valid
//            @RequestBody PasswordResetRequest request) throws PasswordResetTokenNotFoundException, PasswordsAreNotMatchingException, PasswordResetTokenExpiredException {
//        passwordResetService.resetPassword(request);
//        return new ResponseEntity<>(new CustomResponse<>(null, "User password reset.", HttpStatus.OK), HttpStatus.OK);
//    }
//
//    @PostMapping(value = "/auth/password-reset/check")
//    @PreAuthorize("permitAll()")
//    @Operation(
//            summary = "Check code",
//            description = "Check if provided code for password reset is correct")
//    public ResponseEntity<CustomResponse<String>> checkPasswordResetCode(
//            @Valid
//            @RequestBody PasswordResetTokenCheckRequest request) throws UserNotFoundException, PasswordResetTokenAlreadyGeneratedException, AccountNotActivatedException, PasswordResetTokenNotFoundException, PasswordResetTokenExpiredException, PasswordResetEmailSendingException {
//        passwordResetCheckCodeService.checkPasswordResetCode(request);
//        return new ResponseEntity<>(new CustomResponse<>(null, "Code is correct.", HttpStatus.OK), HttpStatus.OK);
//    }
//}
//
//
