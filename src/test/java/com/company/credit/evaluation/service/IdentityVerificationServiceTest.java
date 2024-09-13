package com.company.credit.evaluation.service;

import com.company.credit.evaluation.dto.request.UserIdentityVerificationRequest;
import com.company.credit.evaluation.service.client.ICAGovernmentClient;
import com.company.credit.evaluation.model.CreditCardRequest;
import com.company.credit.evaluation.repository.CreditCardRequestRepository;
import com.company.credit.evaluation.service.exception.ResourceDuplicateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class IdentityVerificationServiceTest {

    @Mock
    private ICAGovernmentClient icaGovernmentClient;

    @Mock
    private CreditCardRequestRepository creditCardRequestRepository;

    @InjectMocks
    private IdentityVerificationService identityVerificationService;

    private String fullName;
    private String emiratesId;
    private UserIdentityVerificationRequest request;
    private CreditCardRequest expectedRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        fullName = "John Doe";
        emiratesId = "784-1234-1234567-1";
        request = new UserIdentityVerificationRequest(fullName, emiratesId);
        expectedRequest = CreditCardRequest.builder()
                .identityVerified(true)
                .fullName(fullName)
                .emiratesId(emiratesId)
                .build();
    }

    @Test
    public void testVerifyIdentity_Success() {
        when(creditCardRequestRepository.findByEmiratesId(emiratesId)).thenReturn(Optional.empty());
        when(icaGovernmentClient.verifyIdentity(fullName, emiratesId)).thenReturn(true);
        when(creditCardRequestRepository.save(any(CreditCardRequest.class))).thenReturn(expectedRequest);

        CreditCardRequest result = identityVerificationService.verifyIdentity(request);

        assertTrue(result.isIdentityVerified());
        assertEquals(fullName, result.getFullName());
        assertEquals(emiratesId, result.getEmiratesId());

        verify(creditCardRequestRepository, times(1)).save(argThat(savedRequest ->
                savedRequest.isIdentityVerified() &&
                        savedRequest.getFullName().equals(fullName) &&
                        savedRequest.getEmiratesId().equals(emiratesId)
        ));
    }

    @Test
    public void testVerifyIdentity_DuplicateRequest() {
        when(creditCardRequestRepository.findByEmiratesId(emiratesId)).thenReturn(Optional.of(new CreditCardRequest()));

        ResourceDuplicateException thrown = assertThrows(ResourceDuplicateException.class, () ->
                identityVerificationService.verifyIdentity(request));
        assertEquals("User against this emirates ID is already processed", thrown.getMessage());
        verify(icaGovernmentClient, never()).verifyIdentity(anyString(), anyString());
        verify(creditCardRequestRepository, never()).save(any());
    }

    @Test
    public void testVerifyIdentity_Failure() {
        when(creditCardRequestRepository.findByEmiratesId(emiratesId)).thenReturn(Optional.empty());
        when(icaGovernmentClient.verifyIdentity(fullName, emiratesId)).thenReturn(false);

        CreditCardRequest result = identityVerificationService.verifyIdentity(request);

        assertFalse(result.isIdentityVerified());
        assertNull(result.getFullName());
        assertNull(result.getEmiratesId());

        verify(creditCardRequestRepository, never()).save(any());
    }
}
