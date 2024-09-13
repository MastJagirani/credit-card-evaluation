package com.company.credit.evaluation.service.client;

public interface AECBClient {
    int getRiskScore(String applicantId, Double requestedCreditLimit, Double annualIncome);
}
