package com.easyjob.easyjobapi.modules.applierProfile;

import com.easyjob.easyjobapi.modules.applierProfile.models.ApplierProfileResponse;
import com.easyjob.easyjobapi.modules.applierProfile.services.ApplierProfileGenerateCVService;
import com.easyjob.easyjobapi.modules.applierProfile.services.ApplierProfileGetService;
import com.easyjob.easyjobapi.utils.CustomResponse;
import com.easyjob.easyjobapi.utils.enums.CVTemplateEnum;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/applier-profile")
@RequiredArgsConstructor
public class ApplierProfileController {
    private final ApplierProfileGetService applierProfileGetService;
    private final ApplierProfileGenerateCVService applierProfileGenerateCVService;

    private final static String DEFAULT_RESPONSE = "Operation successful!";

    @GetMapping(value = "/")
    @Operation(
            description = "Get user applier profile",
            summary = "Get user applier profile"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<ApplierProfileResponse>> get(){
        ApplierProfileResponse response = applierProfileGetService.get();

        return new ResponseEntity<>(new CustomResponse<>(response, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping(value = "/cv")
    @Operation(
            description = "Create CV based on applier profile",
            summary = "Create CV based on applier profile"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<String>> createCV(
            @RequestParam(name = "template")CVTemplateEnum template
            ) {
        applierProfileGenerateCVService.generate(template);

        return new ResponseEntity<>(new CustomResponse<>(DEFAULT_RESPONSE, "CV generation started!", HttpStatus.ACCEPTED),
                HttpStatus.ACCEPTED);
    }

}
