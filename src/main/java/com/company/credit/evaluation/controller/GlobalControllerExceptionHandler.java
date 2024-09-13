package com.company.credit.evaluation.controller;

import com.company.credit.evaluation.dto.response.ErrorDto;
import com.company.credit.evaluation.dto.response.ErrorResponseDto;
import com.company.credit.evaluation.service.exception.EvaluationServiceError;
import com.company.credit.evaluation.service.exception.ResourceDuplicateException;
import com.company.credit.evaluation.service.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Global exception handler for handling various exceptions across the application.
 * This class provides centralized exception handling and generates consistent error responses.
 * handles @Valid annotation errors
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles validation errors that occur when request parameters are invalid.
     *
     * @param ex      the exception containing validation errors
     * @param headers HTTP headers
     * @param status  the HTTP status code
     * @param request the web request
     * @return a {@code ResponseEntity} containing a map of field errors and their messages
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, headers, status);

    }

    /**
     * Handles unknown exceptions that are not specifically caught by other handlers.
     *
     * @param ex      the exception to handle
     * @param request the web request
     * @return a {@code ResponseEntity} containing a generic error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnknownExceptions(Exception ex, WebRequest request) {
        log.error("Unknown Exception:", ex);
        return handleExceptionInternal(ex,
                new ErrorResponseDto(List.of(new ErrorDto(EvaluationServiceError.UNEXPECTED))), new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Handles exceptions where a requested resource is not found.
     *
     * @param ex      the exception to handle
     * @param request the web request
     * @return a {@code ResponseEntity} containing an error response indicating resource not found
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(Exception ex, WebRequest request) {
        log.error("ResourceNotFoundException Exception:", ex);
        return handleExceptionInternal(ex,
                new ErrorResponseDto(List.of(new ErrorDto(EvaluationServiceError.NOT_FOUND))), new HttpHeaders(),
                HttpStatus.NOT_FOUND, request);
    }

    /**
     * Handles exceptions where a resource is found to be a duplicate.
     *
     * @param ex      the exception to handle
     * @param request the web request
     * @return a {@code ResponseEntity} containing an error response indicating resource duplication
     */
    @ExceptionHandler(ResourceDuplicateException.class)
    public ResponseEntity<Object> handleResourceDuplicateException(Exception ex, WebRequest request) {
        log.error("ResourceDuplicateException Exception:", ex);
        return handleExceptionInternal(ex,
                new ErrorResponseDto(List.of(new ErrorDto(EvaluationServiceError.DUPLICATE))), new HttpHeaders(),
                HttpStatus.CONFLICT, request);
    }


}
