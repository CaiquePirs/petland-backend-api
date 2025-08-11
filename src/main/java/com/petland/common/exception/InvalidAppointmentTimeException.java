package com.petland.common.exception;

public class InvalidAppointmentTimeException extends RuntimeException {
    public InvalidAppointmentTimeException(String message) {
        super(message);
    }
}
