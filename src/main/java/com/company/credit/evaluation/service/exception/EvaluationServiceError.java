package com.company.credit.evaluation.service.exception;

public enum EvaluationServiceError {

    UNEXPECTED("CC-0001", "Unexpected server error"),
    NOT_FOUND("CC-0002", "Resource not found"),
    DUPLICATE("CC-0003", "Resource Duplication"),

    ;

    public final String code;
    public final String message;

    EvaluationServiceError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
