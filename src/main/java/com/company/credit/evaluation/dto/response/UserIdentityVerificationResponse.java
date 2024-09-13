package com.company.credit.evaluation.dto.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserIdentityVerificationResponse {

    private String id;
    private Boolean identityVerified;

}
