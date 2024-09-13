package com.company.credit.evaluation.service.client.impl;

import com.company.credit.evaluation.service.client.ICAGovernmentClient;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ICAGovernmentClientImpl implements ICAGovernmentClient {

    // Mock blacklisted names and Emirates IDs
    private static final Set<String> BLACKLISTED_NAMES = Set.of(
            "John Doe", "Jane Smith", "Alice Johnson", "Bob Brown"
    );

    //Mock blacklisted eids
    private static final Set<String> BLACKLISTED_EMIRATES_IDS = Set.of(
            "784-1234-1234567-1",
            "784-2345-2345678-2",
            "784-3456-3456789-3"
    );

    @Override
    public Boolean verifyIdentity(String fullName, String emiratesId) {
        String normalizedFullName = fullName.toLowerCase().trim();

        // Check if the name or Emirates ID is blacklisted
        boolean isNameBlacklisted = BLACKLISTED_NAMES.stream()
                .anyMatch(blacklistedName -> blacklistedName.equalsIgnoreCase(normalizedFullName));

        boolean isIdBlacklisted = BLACKLISTED_EMIRATES_IDS.stream()
                .anyMatch(blacklistedId -> blacklistedId.equals(emiratesId));

        return !(isNameBlacklisted || isIdBlacklisted);
    }
}
