package com.easyjob.easyjobapi.modules.applierProfile.submodules.cv;

import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CVEditRequest;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.models.CVPageResponse;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.services.CVDeleteService;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.services.CVEditService;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.services.CVListService;
import com.easyjob.easyjobapi.modules.applierProfile.submodules.cv.services.CVUploadService;
import com.easyjob.easyjobapi.utils.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/applier-profile/{applierProfileId}/cv")
@RequiredArgsConstructor
public class CVController {
    private final CVListService cvListService;
    private final CVDeleteService cvDeleteService;
    private final CVEditService cvEditService;
    private final CVUploadService cvUploadService;

    private final static String DEFAULT_RESPONSE = "Operation successful.";

    @GetMapping("/list")
    @Operation(
            description = "List user's cvs",
            summary = "List user's cvs"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<CVPageResponse>> getCVList(
            @PathVariable(name = "applierProfileId")UUID applierProfileID,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit
    ){
        CVPageResponse response = cvListService.listCV(applierProfileID, page, limit);

        return new ResponseEntity<>(new CustomResponse<>(response, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{cvId}")
    @Operation(
            description = "Delete cv",
            summary = "Delete cv"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> deleteCV(
            @PathVariable(name = "applierProfileId") UUID applierProfileId,
            @PathVariable(name = "cvId") UUID cvId
    ) {
        cvDeleteService.deleteCV(cvId);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping(value = "/{cvId}")
    @Operation(
            description = "Edit cv",
            summary = "Edit cv"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> editCV(
            @PathVariable(name = "cvId") UUID cvID,
            @RequestBody @Valid CVEditRequest editRequest
    ) {
        cvEditService.edit(cvID, editRequest);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    @Operation(
            description = "Upload user's cv",
            summary = "Upload user's cv"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> uploadCV(
            @PathVariable(name = "applierProfileId") UUID applierProfileId,
            @RequestPart(name = "file") MultipartFile file
    ) throws IOException {
        cvUploadService.uploadCV(applierProfileId, file);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }
}
