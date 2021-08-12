package com.jeeneee.realworld.user.exception;


import com.jeeneee.realworld.common.exception.DuplicateException;

public class DuplicateEmailException extends DuplicateException {

    public DuplicateEmailException() {
        super("The email is already being used.");
    }
}
