package com.easyjob.easyjobapi.core.firm;

import com.easyjob.easyjobapi.core.firm.models.FirmPageResponse;
import com.easyjob.easyjobapi.core.firm.models.FirmRequest;
import com.easyjob.easyjobapi.core.firm.models.FirmResponse;
import com.easyjob.easyjobapi.core.firm.services.*;
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
@RequestMapping("/api/firm")
@RequiredArgsConstructor
public class FirmController {
    private final FirmCreateService firmCreateService;
    private final FirmGetService firmGetService;
    private final FirmApplierGetService firmApplierGetService;
    private final FirmEditRequest firmEditRequest;
    private final FirmUploadLogoService firmUploadLogoService;
    private final FirmCheckExistenceService firmCheckExistenceService;
    private final FirmGetByIdService firmGetByIdService;

    private final static String DEFAULT_RESPONSE = "Operation successful!";

    @PostMapping(value = "/")
    @Operation(
            description = "Create firm",
            summary = "Create firm"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> create(
            @RequestBody @Valid FirmRequest request
    ) {
        firmCreateService.create(request);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping(value = "/")
    @Operation(
            description = "Get user firm",
            summary = "Get user firm"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<FirmResponse>> getFirm(){
        FirmResponse response = firmGetService.getUserFirm();

        return new ResponseEntity<>(new CustomResponse<>(response, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping(value = "/{firmId}")
    @Operation(
            description = "Get firm by ID",
            summary = "Get firm by ID"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<FirmResponse>> getFirmById(
            @PathVariable(name = "firmId") UUID firmId
    ) {
        FirmResponse response = firmGetByIdService.getFirmById(firmId);

        return new ResponseEntity<>(new CustomResponse<>(response, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    @Operation(
            description = "Get all firms",
            summary = "Get all firms"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<FirmPageResponse>> getFirmList(
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page
    ){
        FirmPageResponse response = firmApplierGetService.getFirms(page, limit);

        return new ResponseEntity<>(new CustomResponse<>(response, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping(value = "/logo", consumes = {"multipart/form-data"})
    @Operation(
            description = "Upload firm logo",
            summary = "Upload firm logo"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> uploadLogo(
            @RequestParam(name = "file") MultipartFile file
    ) throws IOException {
        firmUploadLogoService.uploadLogo(file);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping(value = "/")
    @Operation(
            description = "Edit firm information",
            summary = "Edit firm information"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> edit(
            @RequestBody @Valid FirmRequest request
    ) {
        firmEditRequest.edit(request);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping(value = "/check")
    @Operation(
            description = "Check if firm exists for logged user",
            summary = "Check if firm exists for logged user"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Boolean>> checkFirm() {
        Boolean response = firmCheckExistenceService.checkIfFirmExists();
        return new ResponseEntity<>(new CustomResponse<>(response, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }
}
