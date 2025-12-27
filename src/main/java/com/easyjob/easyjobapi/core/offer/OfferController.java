package com.easyjob.easyjobapi.core.offer;

import com.easyjob.easyjobapi.core.offer.models.OfferPageResponse;
import com.easyjob.easyjobapi.core.offer.models.OfferRequest;
import com.easyjob.easyjobapi.core.offer.models.OfferResponse;
import com.easyjob.easyjobapi.core.offer.services.*;
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
@RequestMapping("/api/offer")
@RequiredArgsConstructor
public class OfferController {
    private final OfferCreateService offerCreateService;
    private final OfferEditService offerEditService;
    private final OfferGetByFirmService  offerGetByFirmService;
    private final OfferGetByIdService offerGetByIdService;
    private final OfferGetService offerGetService;
    private final OfferGetAllService offerGetAllService;
    private final OfferDeleteService offerDeleteService;

    private final static String DEFAULT_RESPONSE = "Operation successful.";

    @PostMapping(value = "/")
    @Operation(
            description = "Create offer",
            summary = "Create offer"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> createOffer(
            @RequestBody @Valid OfferRequest request
    ) {
        offerCreateService.create(request);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping(value = "/{offerId}")
    @Operation(
            description = "Edit offer",
            summary = "Edit offer"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> editOffer(
            @PathVariable UUID offerId,
            @RequestBody @Valid OfferRequest request
    ) {
        offerEditService.edit(offerId, request);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping(value = "/firm/list")
    @Operation(
            description = "Get firm offers",
            summary = "Get firm offers"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<OfferPageResponse>> getFirmOffers() {
        OfferPageResponse response = offerGetService.getOffers();
        return new ResponseEntity<>(new CustomResponse<>(response, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping(value = "/firm/{firmId}")
    @Operation(
            description = "Get offers by firm",
            summary = "Get offers by firm"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<OfferPageResponse>> getOffersByFirmId(
            @PathVariable UUID firmId
    ) {
        OfferPageResponse response = offerGetByFirmService.getOffersByFirm(firmId);

        return new ResponseEntity<>(new CustomResponse<>(response, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping(value = "/{offerId}")
    @Operation(
            description = "Get offer by ID",
            summary = "Get offer by ID"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<OfferResponse>> getOfferById(
            @PathVariable UUID offerId
    ) {
        OfferResponse response = offerGetByIdService.getOfferById(offerId);

        return new ResponseEntity<>(new CustomResponse<>(response, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    @Operation(
            description = "Get all offers",
            summary = "Get all offers"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<OfferPageResponse>> getAllOffers(
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "page", required = false, defaultValue = "1")int page
    ) {
        OfferPageResponse response = offerGetAllService.getAllOffers(page, limit);

        return new ResponseEntity<>(new CustomResponse<>(response, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{offerId}")
    @Operation(
            description = "Delete offer by ID",
            summary = "Delete offer by ID"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> deleteOfferById(
            @PathVariable UUID offerId
    ) {
        offerDeleteService.deleteJobOffer(offerId);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }
}
