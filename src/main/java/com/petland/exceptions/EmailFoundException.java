package com.petland.exceptions;

public class EmailFoundException extends RuntimeException {
    public EmailFoundException(String message) {
        super(message);
    }
}
