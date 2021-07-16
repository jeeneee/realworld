package com.jeeneee.realworld.common.exception;

import com.jeeneee.realworld.exception.BadRequestException;

public class IllegalParameterException extends BadRequestException {

    private static final String MESSAGE = "Invalid Parameter";

    public IllegalParameterException() {
        super(MESSAGE);
    }
}