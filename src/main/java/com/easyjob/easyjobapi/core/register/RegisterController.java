package com.easyjob.easyjobapi.core.register;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.easyjob.easyjobapi.core.register.management.UserAlreadyExistException;
import com.easyjob.easyjobapi.core.register.models.RegisterRequest;
import com.easyjob.easyjobapi.core.register.services.RegisterService;
import com.easyjob.easyjobapi.utils.CustomResponse;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class RegisterController {
    private final RegisterService registerService;

    @PostMapping(value = "/auth/register")
    @PreAuthorize("permitAll()")
    @Operation(
            summary = "Register user",
            description = "Register user with provided username and password")
    public ResponseEntity<CustomResponse<String>> registerUser(
            @Valid
            @RequestBody RegisterRequest request) throws UserAlreadyExistException, IOException {
        registerService.registerUser(request);
        return new ResponseEntity<>(new CustomResponse<>(null, "User created.", HttpStatus.CREATED),
                                    HttpStatus.CREATED);
    }
}


