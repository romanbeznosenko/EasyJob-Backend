package com.easyjob.easyjobapi.core.accountActivation;//package com.easyjob.easyjobapi.core.accountActivation;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.easyjob.easyjobapi.core.accountActivation.management.AccountActivationTokenNotFoundException;
import com.easyjob.easyjobapi.core.accountActivation.management.AccountAlreadyActivatedException;
import com.easyjob.easyjobapi.core.accountActivation.management.ActivateAccountTokenAlreadyGeneratedException;
import com.easyjob.easyjobapi.core.accountActivation.management.ActivateAccountTokenExpiredException;
import com.easyjob.easyjobapi.core.accountActivation.models.AccountActivationRequest;
import com.easyjob.easyjobapi.core.accountActivation.models.VerificationCodeResendRequest;
import com.easyjob.easyjobapi.core.accountActivation.services.AccountActivationService;
import com.easyjob.easyjobapi.core.accountActivation.services.VerificationCodeResendService;
import com.easyjob.easyjobapi.core.user.management.UserNotFoundException;
import com.easyjob.easyjobapi.utils.CustomResponse;

@RestController
@RequiredArgsConstructor
public class AccountActivateController {
    private final AccountActivationService accountActivationService;
    private final VerificationCodeResendService verificationCodeResendService;

    @PostMapping(value = "/auth/activate")
    @PreAuthorize("permitAll()")
    @Operation(
            summary = "Activate user account",
            description = "Activate user account")
    public ResponseEntity<CustomResponse<String>> activateUserAccount(
            @Valid
            @RequestBody AccountActivationRequest request) throws UserNotFoundException, AccountAlreadyActivatedException, ActivateAccountTokenAlreadyGeneratedException, AccountActivationTokenNotFoundException, ActivateAccountTokenExpiredException {

        accountActivationService.activateUser(request);
        return new ResponseEntity<>(new CustomResponse<>(null, "Account activated.", HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping(value = "/auth/activate/resend")
    @PreAuthorize("permitAll()")
    @Operation(
            summary = "Resend activation code",
            description = "Resend user account activation code")
    public ResponseEntity<CustomResponse<String>> resendAccountActivationEmail(
            @Valid
            @RequestBody VerificationCodeResendRequest request) throws UserNotFoundException, AccountAlreadyActivatedException, ActivateAccountTokenAlreadyGeneratedException {
        verificationCodeResendService.resendVerificationCode(request);
        return new ResponseEntity<>(new CustomResponse<>(null, "Account activation code resent.", HttpStatus.OK),
                                    HttpStatus.OK);
    }
}


