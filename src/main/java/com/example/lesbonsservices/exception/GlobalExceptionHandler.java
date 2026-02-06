package com.example.lesbonsservices.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

/**
 * A global exception handler for handling application-specific exceptions and returning
 * consistent error responses.
 *
 * This class uses the {@link RestControllerAdvice} annotation to provide centralized
 * exception handling across all {@code @RestController} components.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles the {@link EmailAlreadyUsedException} by returning a response with detailed error information.
     * This method constructs an {@link ApiError} object with relevant details about the exception and HTTP request.
     *
     * @param ex the {@link EmailAlreadyUsedException} that was thrown
     * @param request the {@link HttpServletRequest} associated with the current request
     * @return a {@link ResponseEntity} containing an {@link ApiError} object with details about the error
     */
    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<ApiError> handleEmailAlreadyUsed(EmailAlreadyUsedException ex, HttpServletRequest request){
        HttpStatus status = HttpStatus.CONFLICT;
        ApiError apiError =new ApiError(
                Instant.now(),
                status.getReasonPhrase(),
                "Email already exists",
                ex.getMessage(),
                request.getRequestURI(),
                java.util.UUID.randomUUID().toString()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

}
