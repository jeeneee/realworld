package com.jeeneee.realworld.comment.exception;

import com.jeeneee.realworld.common.exception.NotFoundException;

public class CommentNotFoundException extends NotFoundException {

    public CommentNotFoundException() {
        super("The comment does not exist.");
    }
}
