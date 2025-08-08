package com.petland.common.exception;

public class FailedToGeneratePdfException extends RuntimeException {
    public FailedToGeneratePdfException(String message) {
        super(message);
    }
}
