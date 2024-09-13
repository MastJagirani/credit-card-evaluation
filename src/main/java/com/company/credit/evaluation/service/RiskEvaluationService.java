package com.company.credit.evaluation.service;

import com.company.credit.evaluation.service.client.AECBClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RiskEvaluationService {

    private final AECBClient aecbClient;

    @Autowired
    public RiskEvaluationService(AECBClient aecbClient) {
        this.aecbClient = aecbClient;
    }

    public int evaluateRisk(String applicantId, Double requestedCreditLimit, Double annualIncome) {
        // Get risk score from the client

        return aecbClient.getRiskScore(applicantId, requestedCreditLimit, annualIncome);
    }
}
