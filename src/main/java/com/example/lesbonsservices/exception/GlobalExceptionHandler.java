package com.example.lesbonsservices.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
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

        return ResponseEntity.status(status).body(apiError);
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
                                error.getDefaultMessage() : "Validation error",
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

        return ResponseEntity.status(status).body(apiError);
    }


    /**
     * Handles business-level bad requests outside DTO validation.
     *
     * Used when the request is technically valid but violates functional rules
     * (e.g. invalid state transition, inconsistent parameters, etc.).
     *
     * Logging level: WARN, since it is not a server bug.
     *
     * @param ex      the business exception
     * @param request the current HTTP request
     * @return a 400 ApiError response
     */
    @ExceptionHandler(BadRequestUsedException.class)
    public ResponseEntity<ApiError>  handleBadRequestUsedException(BadRequestUsedException ex, HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiError apiError = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                java.util.UUID.randomUUID().toString(),
                null
        );
        logger.warn("[{}] Bad request on {} : {}",
                apiError.requestId(), apiError.path(), ex.getMessage()
        );

        return ResponseEntity.status(status).body(apiError);
    }

    /**
     * Handles unknown endpoints (no matching route found).
     *
     * Triggered when a client calls an endpoint that does not exist.
     * Returns a consistent 404 ApiError response.
     *
     * Logging level: INFO, since this can happen frequently (typos, outdated routes, bots, etc.).
     *
     * @param ex      the Spring NoHandlerFoundException
     * @param request the current HTTP request
     * @return a 404 ApiError response
     */
    @ExceptionHandler(NoHandlerFoundException .class)
    public ResponseEntity<ApiError> handleNotFound(NoHandlerFoundException  ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiError apiError = new ApiError(
            Instant.now(),
            status.value(),
            status.getReasonPhrase(),
            "Endpoint not found",
            request.getRequestURI(),
            java.util.UUID.randomUUID().toString(),
            null
        );

         logger.info("[{}] No endpoint {} {}",
                 apiError.requestId(),
                 ex.getHttpMethod(),
                 ex.getRequestURL()
         );

         return ResponseEntity.status(status).body(apiError);
    }


    /**
     * Handles {@link ResponseStatusException} and returns a standardized {@link ApiError} response.
     *
     * This handler ensures that exceptions carrying an HTTP status are properly mapped
     * without being transformed into generic 500 errors by the global handler.
     *
     * @param ex      the ResponseStatusException containing status and reason
     * @param request the current HTTP request
     * @return a ResponseEntity containing the ApiError and the corresponding HTTP status
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatusException(
            ResponseStatusException ex,
            HttpServletRequest request
    ) {
        HttpStatusCode statusCode = ex.getStatusCode();
        String reasonPhrase = (statusCode instanceof HttpStatus hs)
                ? hs.getReasonPhrase()
                : "Error";
        ApiError apiError = new ApiError(
                Instant.now(),
                statusCode.value(),
                reasonPhrase,
                ex.getReason(),
                request.getRequestURI(),
                java.util.UUID.randomUUID().toString(),
                null
        );

        logger.warn("[{}] {} on {} : {}",
                apiError.requestId(), statusCode, apiError.path(), ex.getReason()
        );

        return ResponseEntity.status(statusCode).body(apiError);
    }


    /**
     * Safety net: handles any unexpected exception not covered by more specific handlers.
     *
     * This ensures the API always returns a consistent {@link ApiError} response,
     * even when an unhandled runtime exception occurs (NullPointerException,
     * IllegalStateException, etc.).
     *
     * The client receives a generic message while the full stacktrace is logged
     * on the server for debugging purposes.
     *
     * Logging level: ERROR, since this indicates a server-side failure.
     *
     * @param ex      the unexpected exception
     * @param request the current HTTP request
     * @return a standardized 500 ApiError response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAny(Exception ex, HttpServletRequest request){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiError apiError = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                "Unexpected error",
                request.getRequestURI(),
                java.util.UUID.randomUUID().toString(),
                null
        );

        logger.error("[{}] Unhandled error on {}",
                apiError.requestId(), apiError.path(), ex);

        return ResponseEntity.status(status).body(apiError);
    }

}
