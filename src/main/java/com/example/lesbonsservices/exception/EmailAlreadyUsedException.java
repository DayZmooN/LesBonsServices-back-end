package com.example.lesbonsservices.exception;

public class EmailAlreadyUsedException  extends RuntimeException{
    public EmailAlreadyUsedException(String message) {
        super(message);
    }
}
