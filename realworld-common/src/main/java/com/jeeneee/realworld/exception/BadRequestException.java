package com.jeeneee.realworld.exception;

public class BadRequestException extends RuntimeException {

    private static final String MESSAGE = "Invalid Request Error: ";

    public BadRequestException(String message) {
        super(MESSAGE + message);
    }
}
