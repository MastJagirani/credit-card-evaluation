package com.company.credit.evaluation.service.client.impl;

import com.company.credit.evaluation.service.client.AECBClient;
import org.springframework.stereotype.Component;

@Component
public class AECBClientImpl implements AECBClient {

    @Override
    public int getRiskScore(String applicantId, Double requestedCreditLimit, Double annualIncome) {
        if (requestedCreditLimit < annualIncome * 0.5) {
            // Very low risk: credit limit is less than half the income
            return 80;
        } else if (requestedCreditLimit < annualIncome) {
            // Low risk: credit limit is less than the income but more than half the income
            return 65;
        } else if (requestedCreditLimit <= annualIncome * 2) {
            // Medium risk: credit limit is close to or equal to the income
            return 35;
        } else {
            // High risk: credit limit is more than twice the income
            return 10;
        }
    }

}
