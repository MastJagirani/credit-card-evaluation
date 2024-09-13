package com.company.credit.evaluation.dto.response;

import com.company.credit.evaluation.service.exception.EvaluationServiceError;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDto {
    private final String code;
    private final String message;

    public ErrorDto(EvaluationServiceError error) {
        this.code = error.code;
        this.message = error.message;
    }
}
