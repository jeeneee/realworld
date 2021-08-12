package com.jeeneee.realworld.article.exception;

import com.jeeneee.realworld.common.exception.DuplicateException;

public class DuplicateSlugException extends DuplicateException {

    public DuplicateSlugException(String message) {
        super(message);
    }
}
