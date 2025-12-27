package com.easyjob.easyjobapi.core.offerApplicationEvaluation;

import com.easyjob.easyjobapi.core.offerApplicationEvaluation.models.OfferApplicationEvaluationResponse;
import com.easyjob.easyjobapi.core.offerApplicationEvaluation.services.OfferApplicationEvaluationEvaluateService;
import com.easyjob.easyjobapi.core.offerApplicationEvaluation.services.OfferApplicationEvaluationGetService;
import com.easyjob.easyjobapi.utils.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/offer-application/{offerApplicationId}/evaluation")
@RequiredArgsConstructor
public class OfferApplicationEvaluationController {
    private final OfferApplicationEvaluationEvaluateService offerApplicationEvaluationEvaluateService;
    private final OfferApplicationEvaluationGetService offerApplicationEvaluationGetService;

    private static final String EVALUATION_STARTED = "Evaluation started successfully. The process will complete asynchronously.";
    private static final String DEFAULT_RESPONSE = "Operation successful.";

    @PostMapping(value = {"", "/"})
    @Operation(
            description = "Start candidate evaluation process - returns immediately while evaluation runs asynchronously",
            summary = "Evaluate candidate suitability"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<Void>> evaluate(
            @PathVariable(name = "offerApplicationId") UUID offerApplicationId
    ) {
        offerApplicationEvaluationEvaluateService.evaluate(offerApplicationId);

        return new ResponseEntity<>(
                new CustomResponse<>(null, EVALUATION_STARTED, HttpStatus.ACCEPTED),
                HttpStatus.ACCEPTED
        );
    }

    @GetMapping(value = "/")
    @Operation(
            description = "Get candidate's evaluation",
            summary = "Get candidate's evaluation"
    )
    @PreAuthorize("permitAll()")
    public ResponseEntity<CustomResponse<OfferApplicationEvaluationResponse>> get(
            @PathVariable("offerApplicationId") UUID offerApplicationId
    ) {
        OfferApplicationEvaluationResponse response = offerApplicationEvaluationGetService.getByOfferApplicationId(offerApplicationId);

        return new ResponseEntity<>(new CustomResponse<>(response, DEFAULT_RESPONSE, HttpStatus.OK), HttpStatus.OK);
    }
}