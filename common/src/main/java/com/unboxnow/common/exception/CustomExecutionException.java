package com.unboxnow.common.exception;

public class CustomExecutionException extends RuntimeException {

    public CustomExecutionException(String className, String topic, String cause) {
        super(String.format("object -> %s, topic -> %s, reason -> %s",
                className,
                topic,
                cause));
    }
}
