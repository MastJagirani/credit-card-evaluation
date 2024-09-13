package com.company.credit.evaluation.service.client.impl;

import com.company.credit.evaluation.enums.EmploymentType;
import com.company.credit.evaluation.service.client.MinistryOfLabourClient;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Component
public class MinistryOfLabourClientImpl implements MinistryOfLabourClient {

    // Assume these firms are verified in ministry of labor records
    private static final List<String> VERIFIED_FIRMS = List.of("Microsoft", "Google", "Amazon", "Facebook", "Twitter");

    // Assume only these employee types are valid
    private static final List<EmploymentType> VALID_EMPLOYEE_TYPES = List.of(EmploymentType.FULL_TIME, EmploymentType.CONTRACT);

    @Override
    public boolean verifyEmployment(String employer, LocalDate dateOfJoining, EmploymentType employeeType) {
        return VERIFIED_FIRMS.contains(Optional.ofNullable(employer).orElse(""))
                && verifyTenure(Optional.ofNullable(dateOfJoining).orElse(LocalDate.now()))
                && VALID_EMPLOYEE_TYPES.contains(employeeType);
    }

    private boolean verifyTenure(LocalDate dateOfJoining) {
        return !dateOfJoining.isAfter(LocalDate.now())
                && ChronoUnit.MONTHS.between(dateOfJoining, LocalDate.now()) >= 3;
    }
}
