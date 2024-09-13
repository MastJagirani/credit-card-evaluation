package com.company.credit.evaluation.service.client;

import com.company.credit.evaluation.enums.EmploymentType;

import java.time.LocalDate;

public interface MinistryOfLabourClient {
    boolean verifyEmployment(String employer, LocalDate dateOfJoining, EmploymentType employeeType);
}
