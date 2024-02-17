package com.unboxnow.common.exception;

public class NotRespondedException extends RuntimeException {

    public NotRespondedException(String className, String topic) {
        super(String.format("object -> %s, topic -> %s, reason -> no response",
                className,
                topic
        ));
    }
}
