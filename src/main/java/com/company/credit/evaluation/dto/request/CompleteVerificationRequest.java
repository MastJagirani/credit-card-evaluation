package com.company.credit.evaluation.dto.request;

import com.company.credit.evaluation.enums.EmploymentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompleteVerificationRequest {

    @NotBlank(message = "nationality is required")
    @NotNull
    private String nationality;

    @NotBlank(message = "mobile is required")
    @NotNull
    private String mobile;

    @NotBlank(message = "address is required")
    @NotNull
    private String address;

    @NotBlank(message = "company is required")
    @NotNull
    private String company;

    @NotNull(message = "joinDate is required")
    private LocalDate joinDate;

    @NotNull(message = "employmentType is required")
    private EmploymentType employmentType;

    @NotNull(message = "annualIncome is required")
    @Positive(message = "annualIncome should be positive")
    private Double annualIncome;

    @NotNull(message = "requestedCreditLimit is required")
    @Positive(message = "requestedCreditLimit should be >0")
    private Double requestedCreditLimit;


}
