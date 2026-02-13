package com.example.lesbonsservices.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A global exception handler for handling application-specific exceptions and returning
 * consistent error responses.
 * This class uses the {@link RestControllerAdvice} annotation to provide centralized
 * exception handling across all {@code @RestController} components.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /**
     * Handles the case where an email address is already registered.
     *
     * Returns a standardized {@link ApiError} response with HTTP 409 (Conflict).
     * A unique requestId is generated to help correlate logs with client responses.
     *
     * Logging level: WARN, since this is a business rule violation and not a server failure.
     *
     * @param ex      the exception thrown when the email is already used
     * @param request the current HTTP request
     * @return a 409 response containing a consistent ApiError payload
     */
    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<ApiError> handleEmailAlreadyUsed(EmailAlreadyUsedException ex, HttpServletRequest request){
        HttpStatus status = HttpStatus.CONFLICT;

        ApiError apiError =new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                java.util.UUID.randomUUID().toString(),
                null
        );
        logger.warn("[{}] Email already used on {} : {}",
                apiError.requestId(), apiError.path(), ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }



    /**
     * Handles validation errors triggered by DTOs annotated with {@code @Valid}.
     *
     * Example cases: missing fields, invalid formats, failed regex patterns, etc.
     *
     * The response contains:
     * - HTTP 400 (Bad Request)
     * - a generic message ("Validation failed")
     * - a structured map of fieldErrors grouped by field name
     *
     * Logging level: WARN, as validation failures are typically caused by client input.
     *
     * @param ex      the validation exception thrown by Spring
     * @param request the current HTTP request
     * @return a 400 ApiError response with detailed field validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;

        Map<String, List<String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(
                        error -> error.getDefaultMessage() != null ?
                                error.getDefaultMessage() : "Error validation",
                        Collectors.toList()
                        )
                ));

        ApiError apiError =new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                "Validation failed",
                request.getRequestURI(),
                java.util.UUID.randomUUID().toString(),
                errors
        );
        logger.warn("[{}] Validation error on {} : {}",
                apiError.requestId(), apiError.path(), errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }


}
