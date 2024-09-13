package com.company.credit.evaluation.service.client.impl;

import com.company.credit.evaluation.service.client.ComplianceClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class ComplianceClientImpl implements ComplianceClient {

    // Mock blacklisted names
    private static final List<String> BLACKLISTED_NAMES = List.of(
            "Ali Hassan", "Vicky Joseph", "Suresh Kumar", "Jason Statham"
    );

    //Mock blacklisted eids
    private static final Set<String> BLACKLISTED_EMIRATES_IDS = Set.of(
            "784-1111-1234567-1",
            "784-2222-2345678-2",
            "784-3333-3456789-3"
    );

    @Override
    public boolean isNotBlacklisted(String applicantId, String idType) {
        return switch (idType) {
            case "NAME" -> !BLACKLISTED_NAMES.contains(applicantId);
            case "EMIRATES_ID" -> !BLACKLISTED_EMIRATES_IDS.contains(applicantId);
            default -> false;
        };
    }
}
