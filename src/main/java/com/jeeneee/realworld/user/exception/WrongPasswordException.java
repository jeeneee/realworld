package com.jeeneee.realworld.user.exception;

import com.jeeneee.realworld.common.exception.BadRequestException;

public class WrongPasswordException extends BadRequestException {

    public WrongPasswordException() {
        super("wrong password");
    }
}
