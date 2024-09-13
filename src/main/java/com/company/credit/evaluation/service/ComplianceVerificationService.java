package com.company.credit.evaluation.service;

import com.company.credit.evaluation.service.client.ComplianceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplianceVerificationService {

    private final ComplianceClient complianceClient;

    @Autowired
    public ComplianceVerificationService(ComplianceClient complianceClient) {
        this.complianceClient = complianceClient;
    }

    public boolean isApplicantNotBlacklisted(String emiratesId, String fullName) {
        return complianceClient.isNotBlacklisted(emiratesId, "EMIRATES_ID") ||
                complianceClient.isNotBlacklisted(fullName, "NAME");
    }
}
