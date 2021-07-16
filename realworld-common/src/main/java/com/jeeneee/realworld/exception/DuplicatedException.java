package com.jeeneee.realworld.exception;

public class DuplicatedException extends RuntimeException {

    private static final String MESSAGE = "Duplicated Error: ";

    public DuplicatedException(String message) {
        super(MESSAGE + message);
    }
}