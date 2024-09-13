package com.company.credit.evaluation.service.client;

public interface ComplianceClient {
    boolean isNotBlacklisted(String applicantId, String idType);
}
