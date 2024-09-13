package com.company.credit.evaluation.controller;

import com.company.credit.evaluation.dto.request.UserIdentityVerificationRequest;
import com.company.credit.evaluation.dto.response.UserIdentityVerificationResponse;
import com.company.credit.evaluation.model.CreditCardRequest;
import com.company.credit.evaluation.service.IdentityVerificationService;
import com.company.credit.evaluation.utils.ObjectMappingUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling identity verification requests for credit card applicants.
 * This controller processes the verification of applicant details such as Emirates ID and Name.
 */
@Slf4j
@RestController
@RequestMapping(VersionContextPath.V1 + "/credit-card/identity-verification")
public class IdentityVerificationController {

    private final IdentityVerificationService identityVerificationService;

    @Autowired
    public IdentityVerificationController(IdentityVerificationService identityVerificationService) {
        this.identityVerificationService = identityVerificationService;
    }

    /**
     * Verifies the identity of a credit card applicant using their basic details.
     *
     * @param userIdentityVerification the request object containing the applicant's details to be verified
     * @return a {@code ResponseEntity} containing the verification result with a status of 201 (Created) if successful
     * or 400 (Bad Request) if the input is invalid, or 409 (Conflict) if the resource already exists
     */
    @Operation(summary = "Verify Credit Card Applicant Basic Details", description = "Verify Credit Card Applicant Basic Details i.e Emirates Id and Name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful Execution with verification result",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserIdentityVerificationResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Input",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Resource Conflict",
                    content = @Content),
    })
    @PostMapping
    public ResponseEntity<UserIdentityVerificationResponse> verifyIdentity(@RequestBody @Valid UserIdentityVerificationRequest userIdentityVerification) {

        log.info("verifyIdentity : {}", userIdentityVerification);
        CreditCardRequest creditCardRequest =
                identityVerificationService.verifyIdentity(userIdentityVerification);

        return ResponseEntity.ok(ObjectMappingUtils.mapValues(creditCardRequest, UserIdentityVerificationResponse.class));
    }
}
