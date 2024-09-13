package com.company.credit.evaluation.service;

import com.company.credit.evaluation.dto.common.VerificationResults;
import com.company.credit.evaluation.dto.request.CompleteVerificationRequest;
import com.company.credit.evaluation.dto.response.CompleteVerificationResponse;
import com.company.credit.evaluation.enums.OutcomeType;
import com.company.credit.evaluation.model.CreditCardRequest;
import com.company.credit.evaluation.repository.CreditCardRequestRepository;
import com.company.credit.evaluation.service.exception.ResourceDuplicateException;
import com.company.credit.evaluation.service.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class CompleteVerificationService {

    private final CreditCardRequestRepository creditCardRequestRepository;
    private final EmploymentVerificationService employmentVerificationService;
    private final ComplianceVerificationService complianceVerificationService;
    private final RiskEvaluationService riskEvaluationService;
    private final BehavioralAnalysisService behavioralAnalysisService;

    public CompleteVerificationService(CreditCardRequestRepository creditCardRequestRepository,
                                       EmploymentVerificationService employmentVerificationService,
                                       ComplianceVerificationService complianceVerificationService,
                                       RiskEvaluationService riskEvaluationService,
                                       BehavioralAnalysisService behavioralAnalysisService) {
        this.creditCardRequestRepository = creditCardRequestRepository;
        this.employmentVerificationService = employmentVerificationService;
        this.complianceVerificationService = complianceVerificationService;
        this.riskEvaluationService = riskEvaluationService;
        this.behavioralAnalysisService = behavioralAnalysisService;
    }

    public CompleteVerificationResponse performVerifications(
            UUID uuid,
            CompleteVerificationRequest completeVerificationRequest) {

        //check duplicate request
        creditCardRequestRepository.findById(uuid).
                flatMap(creditCardRequest -> Optional.ofNullable(creditCardRequest.getOutcomeType())).ifPresent(outcomeType -> {
                    throw new ResourceDuplicateException("Resource Consumed already");
                });

        CreditCardRequest entity = getCreditCardRequest(uuid);

        CompletableFuture<Boolean> employmentVerifiedFuture = CompletableFuture.supplyAsync(() ->
                employmentVerificationService.verifyEmployment(
                        completeVerificationRequest.getCompany(),
                        completeVerificationRequest.getJoinDate(),
                        completeVerificationRequest.getEmploymentType()));

        CompletableFuture<Boolean> complianceVerifiedFuture = CompletableFuture.supplyAsync(() ->
                complianceVerificationService.isApplicantNotBlacklisted(
                        entity.getEmiratesId(),
                        entity.getFullName()));

        CompletableFuture<Integer> riskScoreFuture = CompletableFuture.supplyAsync(() ->
                riskEvaluationService.evaluateRisk(
                        entity.getEmiratesId(),
                        completeVerificationRequest.getRequestedCreditLimit(),
                        completeVerificationRequest.getAnnualIncome()));

        // assuming file will be uploaded to storage e.g s3, we just give path here
        String uploadedFilePath = "filePath";
        CompletableFuture<Integer> behavioralAnalysisFuture = CompletableFuture.supplyAsync(() ->
                behavioralAnalysisService.analyzeBehavior(uploadedFilePath));

        // Collect results from futures
        VerificationResults results = collectVerificationResults(
                employmentVerifiedFuture,
                complianceVerifiedFuture,
                riskScoreFuture,
                behavioralAnalysisFuture);

        // Calculate and set score
        int score = calculateTotalScore(results);
        setEntityFields(entity, completeVerificationRequest, results, score);

        // Determine application outcome
        OutcomeType outcome = determineOutcome(entity.isIdentityVerified(), score);

        // Set outcome in the entity
        entity.setOutcomeType(outcome);

        creditCardRequestRepository.save(entity);

        return buildVerificationResponse(entity, outcome);
    }

    private CreditCardRequest getCreditCardRequest(UUID uuid) {
        return creditCardRequestRepository.findById(uuid)
                .orElseThrow(() -> {
                    log.info("CreditCardRequest Entity not found with ID :{}", uuid);
                    return new ResourceNotFoundException("Resource Not Found Exception : CreditCardRequest ");
                });
    }

    private VerificationResults collectVerificationResults(
            CompletableFuture<Boolean> employmentVerifiedFuture,
            CompletableFuture<Boolean> complianceVerifiedFuture,
            CompletableFuture<Integer> riskScoreFuture,
            CompletableFuture<Integer> behavioralAnalysisFuture) {

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                employmentVerifiedFuture,
                complianceVerifiedFuture,
                riskScoreFuture,
                behavioralAnalysisFuture);

        try {
            allFutures.join(); // Wait for all futures to complete

            return new VerificationResults(
                    employmentVerifiedFuture.get(),
                    complianceVerifiedFuture.get(),
                    riskScoreFuture.get(),
                    behavioralAnalysisFuture.get()
            );

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private int calculateTotalScore(VerificationResults results) {
        int employmentScore = results.isEmploymentVerified() ? 20 : 0;
        int complianceScore = results.isComplianceVerified() ? 20 : 0;

        int riskEvaluationScore = calculateScoreFromPercentage(results.getRiskScore());
        int behavioralAnalysisScore = calculateScoreFromPercentage(results.getBehavioralAnalysis());

        int identityVerification = 20;
        return identityVerification + employmentScore + complianceScore + riskEvaluationScore + behavioralAnalysisScore;
    }

    private int calculateScoreFromPercentage(int percentage) {
        return Math.min(20, (percentage * 20) / 100);
    }

    private void setEntityFields(CreditCardRequest entity,
                                 CompleteVerificationRequest request,
                                 VerificationResults results,
                                 int score) {
        entity.setAnnualIncome(request.getAnnualIncome());
        entity.setCreditScore(score);
        entity.setEmploymentCheck(results.isEmploymentVerified());
        entity.setComplianceCheck(results.isComplianceVerified());
        entity.setRiskEvaluation(results.getRiskScore());
        entity.setBehavioralAnalysis(results.getBehavioralAnalysis());
        entity.setScore(score);
    }

    private OutcomeType determineOutcome(boolean isIdentityVerified, int totalScore) {
        if (!isIdentityVerified || totalScore < 50) {
            return OutcomeType.REJECTED;
        } else if (totalScore >= 90) {
            return OutcomeType.STP;
        } else if (totalScore >= 75) {
            return OutcomeType.NEAR_STP;
        } else {
            return OutcomeType.MANUAL_REVIEW;
        }
    }

    private CompleteVerificationResponse buildVerificationResponse(CreditCardRequest entity, OutcomeType outcome) {
        return CompleteVerificationResponse.builder()
                .outcome(outcome)
                .build();
    }

}