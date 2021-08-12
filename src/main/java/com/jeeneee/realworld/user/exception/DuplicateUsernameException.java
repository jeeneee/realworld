package com.jeeneee.realworld.user.exception;

import com.jeeneee.realworld.common.exception.DuplicateException;

public class DuplicateUsernameException extends DuplicateException {

    public DuplicateUsernameException() {
        super("The username is already being used.");
    }
}
