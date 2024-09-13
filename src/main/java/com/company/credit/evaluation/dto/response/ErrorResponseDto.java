package com.company.credit.evaluation.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
public class ErrorResponseDto {
    private final Boolean success = false;
    private List<ErrorDto> errors;

    public ErrorResponseDto(List<ErrorDto> errors) {
        this.errors = errors;
    }
}
