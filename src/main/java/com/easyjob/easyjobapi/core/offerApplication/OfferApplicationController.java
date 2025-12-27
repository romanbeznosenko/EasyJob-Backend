package com.easyjob.easyjobapi.core.offerApplication;

import com.easyjob.easyjobapi.core.offerApplication.models.OfferApplicationPageResponse;
import com.easyjob.easyjobapi.core.offerApplication.services.OfferApplicationChangeStatusService;
import com.easyjob.easyjobapi.core.offerApplication.services.OfferApplicationCreateService;
import com.easyjob.easyjobapi.core.offerApplication.services.OfferApplicationListService;
import com.easyjob.easyjobapi.core.offerApplication.services.OfferApplicationUserGetService;
import com.easyjob.easyjobapi.utils.CustomResponse;
import com.easyjob.easyjobapi.utils.enums.ApplicationStatusEnum;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/offer-application")
@RequiredArgsConstructor
public class OfferApplicationController {
    private final OfferApplicationCreateService offerApplicationCreateService;
    private final OfferApplicationUserGetService offerApplicationUserGetService;
    private final OfferApplicationChangeStatusService offerApplicationChangeStatusService;
    private final OfferApplicationListService offerApplicationListService;

    private final static String DEFAULT_RESPONSE = "Operation successful.";

    @PostMapping("/offer/{offerId}/apply")
    @Operation(
            description = "Apply for job offer",
            summary = "Apply for job offer"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> applyOffer(
            @PathVariable(name = "offerId")UUID offerId
    ) {
        offerApplicationCreateService.create(offerId);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK),
                HttpStatus.OK);
    }

    @GetMapping("/me/list")
    @Operation(
            description = "Get user offer applications",
            summary = "Get user offer applications"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<OfferApplicationPageResponse>> getUserOfferApplications(
            @RequestParam(name = "limit", required = false, defaultValue = "10")int limit,
            @RequestParam(name = "page", required = false, defaultValue = "1")int page
    ) {
        OfferApplicationPageResponse response = offerApplicationUserGetService.getUserApplication(page, limit);

        return new ResponseEntity<>(new CustomResponse<>(response, DEFAULT_RESPONSE, HttpStatus.OK),
                HttpStatus.OK);
    }

    @PutMapping("/{offerApplicationId}/status")
    @Operation(
            description = "Change offer application status",
            summary = "Change offer application status"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> changeOfferApplicationStatus(
            @PathVariable(name = "offerApplicationId")UUID offerApplicationId,
            @RequestParam(name = "status")ApplicationStatusEnum status
    ) {
        offerApplicationChangeStatusService.changeStatus(offerApplicationId, status);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.OK),
                HttpStatus.OK);
    }

    @GetMapping("/offer/{offerId}/list")
    @Operation(
            description = "List offer applications for job offer",
            summary = "List offer applications for job offer"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<OfferApplicationPageResponse>> getUserOfferApplications(
            @PathVariable(name = "offerId") UUID offerId,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit
    ) {
        OfferApplicationPageResponse response = offerApplicationListService.getOfferApplicationList(offerId, page, limit);

        return new ResponseEntity<>(new CustomResponse<>(response, DEFAULT_RESPONSE, HttpStatus.OK),
                HttpStatus.OK);
    }
}
