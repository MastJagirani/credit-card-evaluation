package com.company.credit.evaluation.dto.response;

import com.company.credit.evaluation.enums.OutcomeType;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompleteVerificationResponse {

    private OutcomeType outcome;
}
