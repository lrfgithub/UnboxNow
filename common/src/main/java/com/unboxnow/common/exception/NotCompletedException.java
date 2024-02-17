package com.unboxnow.common.exception;

public class NotCompletedException extends RuntimeException {

    public NotCompletedException(String username) {
        super(String.format("object -> member, username -> %s, reason -> not completed",
                username
        ));
    }
}
