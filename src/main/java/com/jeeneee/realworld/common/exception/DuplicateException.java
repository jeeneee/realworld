package com.jeeneee.realworld.common.exception;

public class DuplicateException extends RuntimeException {

    private static final String MESSAGE = "Duplicate Error: ";

    public DuplicateException(String message) {
        super(MESSAGE + message);
    }
}