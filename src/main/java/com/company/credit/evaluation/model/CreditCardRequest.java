package com.company.credit.evaluation.model;

import com.company.credit.evaluation.enums.OutcomeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

/**
 * Represents a credit card request in the system.
 * This entity contains all the necessary details for processing a credit card application,
 * including personal information, verification results, and outcome details.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "credit_request")
public class CreditCardRequest {

    /**
     * Unique identifier for the credit card request.
     */
    @Id
    @UuidGenerator
    private UUID id;

    /**
     * Full name of the applicant.
     */
    private String fullName;

    /**
     * Unique Emirates ID of the applicant.
     * This field is indexed to ensure its uniqueness across the database.
     */
    @Column(unique = true)
    private String emiratesId;

    /**
     * Indicates whether the applicant's identity has been verified.
     */
    private boolean identityVerified;

    /**
     * Annual income of the applicant.
     */
    private double annualIncome;

    /**
     * Credit score of the applicant .
     */
    private int creditScore;

    /**
     * Indicates whether employment verification has been verified.
     */
    private boolean employmentCheck;

    /**
     * Indicates whether compliance checks (e.g., blacklist) have been verified.
     */
    private boolean complianceCheck;

    /**
     * Risk evaluation score based on various risk factors.
     */
    private int riskEvaluation;

    /**
     * Behavioral analysis score reflecting the applicant's spending habits.
     */
    private int behavioralAnalysis;

    /**
     * Final score calculated for the credit card request, combining various assessments.
     */
    private int score;

    /**
     * The outcome of the credit card request, based on the evaluation and analysis.
     */
    private OutcomeType outcomeType;
}
