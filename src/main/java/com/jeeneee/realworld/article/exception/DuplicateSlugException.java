package com.jeeneee.realworld.article.exception;

import com.jeeneee.realworld.common.exception.DuplicateException;

public class DuplicateSlugException extends DuplicateException {

    public DuplicateSlugException() {
        super("The article with the slug already exists.");
    }
}
