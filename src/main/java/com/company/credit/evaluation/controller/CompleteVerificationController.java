package com.company.credit.evaluation.controller;

import com.company.credit.evaluation.dto.request.CompleteVerificationRequest;
import com.company.credit.evaluation.dto.response.CompleteVerificationResponse;
import com.company.credit.evaluation.dto.response.UserIdentityVerificationResponse;
import com.company.credit.evaluation.service.CompleteVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller for handling complete verification requests for credit card applications.
 * This includes verifying employment, compliance, spending habits, and risk assessment.
 */
@Slf4j
@RestController
@RequestMapping(VersionContextPath.V1 + "/credit-card/complete-verification")
public class CompleteVerificationController {

    private final CompleteVerificationService completeVerificationService;

    @Autowired
    public CompleteVerificationController(CompleteVerificationService completeVerificationService) {
        this.completeVerificationService = completeVerificationService;
    }

    /**
     * Verifies the applicant's details by performing a series of checks including employment, compliance, spending habits, and risk assessment.
     *
     * @param completeVerificationRequest the request containing details needed for verification
     * @param id                          the unique identifier for the credit card request
     * @return a CompleteVerificationResponse containing the result of the verification process
     */
    @Operation(summary = "Verify Complete User Details", description = "Verify employment , compliance, spending habits, risk")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful Execution with verification result",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserIdentityVerificationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Input",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Resource Conflict",
                    content = @Content),
    })
    @PostMapping("/{id}")
    public ResponseEntity<CompleteVerificationResponse> verifyApplicant(
            @RequestBody CompleteVerificationRequest completeVerificationRequest,
            @PathVariable UUID id
    ) {
        log.info("completeVerificationRequest : {}", completeVerificationRequest);
        CompleteVerificationResponse result = completeVerificationService.performVerifications(id, completeVerificationRequest);
        return ResponseEntity.ok(result);
    }
}
