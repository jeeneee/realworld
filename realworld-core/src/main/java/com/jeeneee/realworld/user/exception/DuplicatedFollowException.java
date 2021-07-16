package com.jeeneee.realworld.user.exception;

import com.jeeneee.realworld.exception.DuplicatedException;

public class DuplicatedFollowException extends DuplicatedException {

    public DuplicatedFollowException(String message) {
        super(message);
    }
}
