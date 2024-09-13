package com.company.credit.evaluation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserIdentityVerificationRequest {

    @NotBlank(message = "fullName is required")
    private String fullName;

    @NotBlank(message = "emiratesId is required")
    private String emiratesId;

}
