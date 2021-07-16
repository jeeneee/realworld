package com.jeeneee.realworld.article.exception;

import com.jeeneee.realworld.exception.DuplicatedException;

public class DuplicatedFavoriteException extends DuplicatedException {

    public DuplicatedFavoriteException(String message) {
        super(message);
    }
}
