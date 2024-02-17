package com.unboxnow.common.exception;

public class TokenException extends RuntimeException {

    public TokenException(String reason) {
        super(String.format("object -> token, reason -> %s", reason));
    }
}
