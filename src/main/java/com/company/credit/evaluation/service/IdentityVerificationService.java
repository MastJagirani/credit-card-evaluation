package com.company.credit.evaluation.service;

import com.company.credit.evaluation.dto.request.UserIdentityVerificationRequest;
import com.company.credit.evaluation.service.client.ICAGovernmentClient;
import com.company.credit.evaluation.model.CreditCardRequest;
import com.company.credit.evaluation.repository.CreditCardRequestRepository;
import com.company.credit.evaluation.service.exception.ResourceDuplicateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IdentityVerificationService {

    private final ICAGovernmentClient icaGovernmentClient;

    private final CreditCardRequestRepository creditCardRequestRepository;

    @Autowired
    public IdentityVerificationService(ICAGovernmentClient icaGovernmentClient, CreditCardRequestRepository creditCardRequestRepository) {
        this.icaGovernmentClient = icaGovernmentClient;
        this.creditCardRequestRepository = creditCardRequestRepository;
    }

    public CreditCardRequest verifyIdentity(UserIdentityVerificationRequest userIdentityVerificationRequest) {
        String fullName = userIdentityVerificationRequest.getFullName();
        String emiratesId = userIdentityVerificationRequest.getEmiratesId();

        creditCardRequestRepository.findByEmiratesId(emiratesId)
                .ifPresent(existingRequest -> {
                    throw new ResourceDuplicateException("User against this emirates ID is already processed");
                });

        boolean isIdentityVerified = icaGovernmentClient.verifyIdentity(fullName, emiratesId);

        return Optional.of(isIdentityVerified)
                .filter(result -> result)
                .map(result -> CreditCardRequest.builder()
                        .identityVerified(true)
                        .fullName(fullName)
                        .emiratesId(emiratesId)
                        .build())
                .map(creditCardRequestRepository::save)
                .orElse(CreditCardRequest.builder().identityVerified(false).build());
    }

}
