package com.easyjob.easyjobapi.core.login;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.easyjob.easyjobapi.core.login.management.AccountNotActivatedException;
import com.easyjob.easyjobapi.core.login.management.IncorrectLoginCredentialsException;
import com.easyjob.easyjobapi.core.login.models.LoginRequest;
import com.easyjob.easyjobapi.core.login.services.LoginService;
import com.easyjob.easyjobapi.core.user.management.UserNotFoundException;
import com.easyjob.easyjobapi.utils.CustomResponse;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping(value = "/auth/login")
    @PreAuthorize("permitAll()")
    @Operation(
            summary = "Login user",
            description = "Login user with provided username and password")
    public ResponseEntity<CustomResponse<Object>> loginUser(
            @Valid
            @RequestBody LoginRequest request) throws UserNotFoundException, IncorrectLoginCredentialsException, AccountNotActivatedException {
        loginService.loginUser(request);

        return new ResponseEntity<>(new CustomResponse<>(null, "Login successful.", HttpStatus.OK),
                                    HttpStatus.OK);
    }
}


