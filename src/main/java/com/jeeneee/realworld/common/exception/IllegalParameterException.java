package com.jeeneee.realworld.common.exception;

public class IllegalParameterException extends BadRequestException {

    private static final String MESSAGE = "Invalid Parameter";

    public IllegalParameterException() {
        super(MESSAGE);
    }
}