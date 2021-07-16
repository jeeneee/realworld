package com.jeeneee.realworld.user.exception;

import com.jeeneee.realworld.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    private static final String MESSAGE = "The user does not exist.";

    public UserNotFoundException() {
        super(MESSAGE);
    }
}
