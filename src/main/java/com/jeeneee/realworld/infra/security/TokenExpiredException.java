package com.jeeneee.realworld.infra.security;

import com.jeeneee.realworld.common.exception.BadRequestException;

public class TokenExpiredException extends BadRequestException {

    public TokenExpiredException(String message) {
        super(message);
    }
}
