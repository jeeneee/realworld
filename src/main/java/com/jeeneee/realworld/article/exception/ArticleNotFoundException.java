package com.jeeneee.realworld.article.exception;

import com.jeeneee.realworld.common.exception.NotFoundException;

public class ArticleNotFoundException extends NotFoundException {

    public ArticleNotFoundException() {
        super("The article does not exist.");
    }
}
