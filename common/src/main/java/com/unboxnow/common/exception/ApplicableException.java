package com.unboxnow.common.exception;

public class ApplicableException extends RuntimeException {

    public ApplicableException(String className, int theId, String recipient) {
        super(String.format("object -> %s, id -> %d, reason -> still applied to %s",
                className,
                theId,
                recipient
        ));
    }
}
