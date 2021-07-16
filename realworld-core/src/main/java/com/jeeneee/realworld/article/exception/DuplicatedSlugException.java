package com.jeeneee.realworld.article.exception;

import com.jeeneee.realworld.exception.DuplicatedException;

public class DuplicatedSlugException extends DuplicatedException {

    public DuplicatedSlugException(String message) {
        super(message);
    }
}
