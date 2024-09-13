package com.company.credit.evaluation.dto.common;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerificationResults {
    private boolean employmentVerified;
    private boolean complianceVerified;
    private int riskScore;
    private int behavioralAnalysis;

}