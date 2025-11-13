package com.easyjob.easyjobapi.modules.applierProfile.submodules.education;

import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models.EducationPageResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.models.EducationRequest;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.services.EducationCreateService;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.services.EducationDeleteService;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.services.EducationEditService;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.education.services.EducationGetService;
import com.easyjob.easyjobapi.utils.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user/applier-profile/education")
@RequiredArgsConstructor
public class EducationController {
    private final EducationCreateService educationCreateService;
    private final EducationEditService educationEditService;
    private final EducationGetService educationGetService;
    private final EducationDeleteService educationDeleteService;

    private final static String DEFAULT_RESPONSE = "Operation successful!";

    @PostMapping(value = "/")
    @Operation(
            description = "Add applier education",
            summary = "Add applier education"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> create(
            @Valid
            @RequestBody
            EducationRequest request
    ) {
        educationCreateService.create(request);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping(value = "/{education-id}")
    @Operation(
            description = "Edit applier education",
            summary = "Edit applier education"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> edit(
            @PathVariable("education-id")UUID educationID,
            @Valid
            @RequestBody
            EducationRequest request
    ) {
        educationEditService.edit(request,educationID);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    @Operation(
            description = "Get applier educations",
            summary = "Get applier educations"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<EducationPageResponse>> get(
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page
    ) {
        EducationPageResponse response = educationGetService.get(limit, page);

        return new ResponseEntity<>(new CustomResponse<>(response, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{education-id}")
    @Operation(
            description = "Delete applier education",
            summary = "Delete applier education"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> delete(
            @PathVariable("education-id") UUID educationID
    ) {
        educationDeleteService.delete(educationID);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

}
