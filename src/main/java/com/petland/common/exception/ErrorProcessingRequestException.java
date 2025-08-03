package com.petland.common.exception;

public class ErrorProcessingRequestException extends RuntimeException {
    public ErrorProcessingRequestException(String message) {
        super(message);
    }
}
