package com.jeeneee.realworld.user.exception;

import com.jeeneee.realworld.common.exception.DuplicateException;

public class DuplicateFollowException extends DuplicateException {

    public DuplicateFollowException() {
        super("The user you have already followed.");
    }
}
