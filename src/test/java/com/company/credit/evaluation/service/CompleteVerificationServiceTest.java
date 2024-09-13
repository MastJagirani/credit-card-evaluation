package com.company.credit.evaluation.service;

import com.company.credit.evaluation.dto.request.CompleteVerificationRequest;
import com.company.credit.evaluation.dto.response.CompleteVerificationResponse;
import com.company.credit.evaluation.enums.EmploymentType;
import com.company.credit.evaluation.enums.OutcomeType;
import com.company.credit.evaluation.model.CreditCardRequest;
import com.company.credit.evaluation.repository.CreditCardRequestRepository;
import com.company.credit.evaluation.service.exception.ResourceDuplicateException;
import com.company.credit.evaluation.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CompleteVerificationServiceTest {

    @Mock
    private CreditCardRequestRepository creditCardRequestRepository;

    @Mock
    private EmploymentVerificationService employmentVerificationService;

    @Mock
    private ComplianceVerificationService complianceVerificationService;

    @Mock
    private RiskEvaluationService riskEvaluationService;

    @Mock
    private BehavioralAnalysisService behavioralAnalysisService;

    @InjectMocks
    private CompleteVerificationService completeVerificationService;

    private UUID uuid;
    private CompleteVerificationRequest request;
    private CreditCardRequest creditCardRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        uuid = UUID.randomUUID();
        request = new CompleteVerificationRequest(
                "UAE", "1234567890", "123 Street", "Company",
                LocalDate.of(2021, 1, 1), EmploymentType.FULL_TIME,
                50000.0, 10000.0
        );
        creditCardRequest = new CreditCardRequest();
        creditCardRequest.setEmiratesId("784-1234-1234567-1");
        creditCardRequest.setFullName("John Doe");
    }

    @Test
    public void testPerformVerifications_OutcomeRejected() {
        creditCardRequest.setIdentityVerified(false);
        when(creditCardRequestRepository.findById(uuid)).thenReturn(Optional.of(creditCardRequest));
        when(employmentVerificationService.verifyEmployment(anyString(), any(), any())).thenReturn(true);
        when(complianceVerificationService.isApplicantNotBlacklisted(anyString(), anyString())).thenReturn(true);
        when(riskEvaluationService.evaluateRisk(anyString(), anyDouble(), anyDouble())).thenReturn(50);
        when(behavioralAnalysisService.analyzeBehavior(anyString())).thenReturn(50);

        CompleteVerificationResponse response = completeVerificationService.performVerifications(uuid, request);

        assertEquals(OutcomeType.REJECTED, response.getOutcome());
        verify(creditCardRequestRepository, times(1)).save(any(CreditCardRequest.class));
    }

    @Test
    public void testPerformVerifications_OutcomeSTP() {
        creditCardRequest.setIdentityVerified(true);
        when(creditCardRequestRepository.findById(uuid)).thenReturn(Optional.of(creditCardRequest));
        when(employmentVerificationService.verifyEmployment(anyString(), any(), any())).thenReturn(true);
        when(complianceVerificationService.isApplicantNotBlacklisted(anyString(), anyString())).thenReturn(true);
        when(riskEvaluationService.evaluateRisk(anyString(), anyDouble(), anyDouble())).thenReturn(95);
        when(behavioralAnalysisService.analyzeBehavior(anyString())).thenReturn(95);

        CompleteVerificationResponse response = completeVerificationService.performVerifications(uuid, request);

        assertEquals(OutcomeType.STP, response.getOutcome());
        verify(creditCardRequestRepository, times(1)).save(any(CreditCardRequest.class));
    }

    @Test
    public void testPerformVerifications_OutcomeNearSTP() {
        creditCardRequest.setIdentityVerified(true);
        when(creditCardRequestRepository.findById(uuid)).thenReturn(Optional.of(creditCardRequest));
        when(employmentVerificationService.verifyEmployment(anyString(), any(), any())).thenReturn(true);
        when(complianceVerificationService.isApplicantNotBlacklisted(anyString(), anyString())).thenReturn(true);
        when(riskEvaluationService.evaluateRisk(anyString(), anyDouble(), anyDouble())).thenReturn(65);
        when(behavioralAnalysisService.analyzeBehavior(anyString())).thenReturn(65);

        CompleteVerificationResponse response = completeVerificationService.performVerifications(uuid, request);

        assertEquals(OutcomeType.NEAR_STP, response.getOutcome());
        verify(creditCardRequestRepository, times(1)).save(any(CreditCardRequest.class));
    }

    @Test
    public void testPerformVerifications_OutcomeManualReview() {
        creditCardRequest.setIdentityVerified(true);
        when(creditCardRequestRepository.findById(uuid)).thenReturn(Optional.of(creditCardRequest));
        when(employmentVerificationService.verifyEmployment(anyString(), any(), any())).thenReturn(true);
        when(complianceVerificationService.isApplicantNotBlacklisted(anyString(), anyString())).thenReturn(true);
        when(riskEvaluationService.evaluateRisk(anyString(), anyDouble(), anyDouble())).thenReturn(30);
        when(behavioralAnalysisService.analyzeBehavior(anyString())).thenReturn(30);

        CompleteVerificationResponse response = completeVerificationService.performVerifications(uuid, request);

        assertEquals(OutcomeType.MANUAL_REVIEW, response.getOutcome());
        verify(creditCardRequestRepository, times(1)).save(any(CreditCardRequest.class));
    }

    @Test
    public void testPerformVerifications_DuplicateRequest() {
        creditCardRequest.setOutcomeType(OutcomeType.REJECTED);
        when(creditCardRequestRepository.findById(uuid)).thenReturn(Optional.of(creditCardRequest));

        ResourceDuplicateException thrown = assertThrows(ResourceDuplicateException.class, () ->
                completeVerificationService.performVerifications(uuid, request));
        assertEquals("Resource Consumed already", thrown.getMessage());
        verify(employmentVerificationService, never()).verifyEmployment(anyString(), any(), any());
        verify(complianceVerificationService, never()).isApplicantNotBlacklisted(anyString(), anyString());
        verify(riskEvaluationService, never()).evaluateRisk(anyString(), anyDouble(), anyDouble());
        verify(behavioralAnalysisService, never()).analyzeBehavior(anyString());
    }

    @Test
    public void testPerformVerifications_ResourceNotFound() {
        when(creditCardRequestRepository.findById(uuid)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () ->
                completeVerificationService.performVerifications(uuid, request));
        assertEquals("Resource Not Found Exception : CreditCardRequest ", thrown.getMessage());
        verify(employmentVerificationService, never()).verifyEmployment(anyString(), any(), any());
        verify(complianceVerificationService, never()).isApplicantNotBlacklisted(anyString(), anyString());
        verify(riskEvaluationService, never()).evaluateRisk(anyString(), anyDouble(), anyDouble());
        verify(behavioralAnalysisService, never()).analyzeBehavior(anyString());
    }
}
