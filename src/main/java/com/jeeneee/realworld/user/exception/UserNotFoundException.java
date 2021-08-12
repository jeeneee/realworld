package com.jeeneee.realworld.user.exception;

import com.jeeneee.realworld.common.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException() {
        super("The user does not exist.");
    }
}
