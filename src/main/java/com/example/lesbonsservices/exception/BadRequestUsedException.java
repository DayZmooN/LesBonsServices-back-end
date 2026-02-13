package com.example.lesbonsservices.exception;

public class BadRequestUsedException extends RuntimeException {
    public BadRequestUsedException(String message) {
        super(message);
    }
}
