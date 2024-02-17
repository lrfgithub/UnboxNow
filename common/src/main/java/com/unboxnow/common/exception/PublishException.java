package com.unboxnow.common.exception;

public class PublishException extends RuntimeException {

    public PublishException(String topic, String message) {
        super(String.format("object -> Producer, topic -> %s, message -> %s", topic, message));
    }
}
