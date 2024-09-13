package com.company.credit.evaluation.service;

import com.company.credit.evaluation.enums.EmploymentType;
import com.company.credit.evaluation.service.client.MinistryOfLabourClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EmploymentVerificationService {

    private final MinistryOfLabourClient ministryOfLabourClient;

    @Autowired
    public EmploymentVerificationService(MinistryOfLabourClient ministryOfLabourClient) {
        this.ministryOfLabourClient = ministryOfLabourClient;
    }

    public boolean verifyEmployment(String employer, LocalDate dateOfJoining, EmploymentType employeeType) {
        return ministryOfLabourClient.verifyEmployment(employer, dateOfJoining, employeeType);
    }
}
