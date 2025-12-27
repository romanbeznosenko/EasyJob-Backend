package com.easyjob.easyjobapi.core.offerApplicationEvaluation;

import com.easyjob.easyjobapi.core.offerApplicationEvaluation.services.OfferApplicationEvaluationEvaluateService;
import com.easyjob.easyjobapi.utils.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/offer-application/{offerApplicationId}/evaluation")
@RequiredArgsConstructor
public class OfferApplicationEvaluationController {
    private final OfferApplicationEvaluationEvaluateService offerApplicationEvaluationEvaluateService;

    private final static String DEFAULT_RESPONSE = "Operation successful.";

    @PostMapping(value = {"", "/"})
    @Operation(
            description = "Evaluate candidate suitability",
            summary = "Evaluate candidate suitability"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> evaluate(
            @PathVariable(name = "offerApplicationId") UUID offerApplicationId
    ) {
        offerApplicationEvaluationEvaluateService.evaluate(offerApplicationId);

        return new ResponseEntity<>(new CustomResponse<>(null, DEFAULT_RESPONSE, HttpStatus.CREATED), HttpStatus.CREATED);
    }
}
