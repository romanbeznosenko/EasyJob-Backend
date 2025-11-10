package com.easyjob.easyjobapi.modules.applierProfile.workExperience;

import com.easyjob.easyjobapi.modules.applierProfile.workExperience.models.WorkExperiencePageResponse;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.models.WorkExperienceRequest;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.models.WorkExperienceResponse;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.services.WorkExperienceCreateService;
import com.easyjob.easyjobapi.modules.applierProfile.workExperience.services.WorkExperienceGetService;
import com.easyjob.easyjobapi.utils.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/applier-profile/work-experience")
@RequiredArgsConstructor
public class WorkExperienceController {
    private final WorkExperienceCreateService workExperienceCreateService;
    private final WorkExperienceGetService workExperienceGetService;

    private final static String DEFAULT_RESPONSE = "Operation successful.";

    @PostMapping(value = "/")
    @Operation(
            description = "Add work experience to applier profile",
            summary = "Add work experience to applier profile"
    )
    @PreAuthorize("hasAuthority('ROLE_APPLIER')")
    public ResponseEntity<CustomResponse<Void>> addWorkExperience(
            @Valid @RequestBody WorkExperienceRequest request
    ){
        workExperienceCreateService.create(request);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK),
                HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    @Operation(
            description = "Get applier work experience",
            summary = "Get applier work experience"
    )
    @PreAuthorize("hasAuthority('ROLE_APPLIER')")
    public ResponseEntity<CustomResponse<WorkExperiencePageResponse>> getWorkExperience(
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page
    ) {
        WorkExperiencePageResponse response = workExperienceGetService.getWorkExperiencePage(limit, page);

        return new ResponseEntity<>(new CustomResponse<>(response, DEFAULT_RESPONSE, HttpStatus.OK),
                HttpStatus.OK);
    }
}
