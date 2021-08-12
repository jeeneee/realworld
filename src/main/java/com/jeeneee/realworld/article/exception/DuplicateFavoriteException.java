package com.jeeneee.realworld.article.exception;

import com.jeeneee.realworld.common.exception.DuplicateException;

public class DuplicateFavoriteException extends DuplicateException {

    public DuplicateFavoriteException(String message) {
        super(message);
    }
}
