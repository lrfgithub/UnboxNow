package com.unboxnow.common.exception;

public class IllegalMessageException extends RuntimeException {

    public IllegalMessageException(String topic, String messageId, String field, String value) {
        super(String.format("topic -> %s, id -> %s, %s -> %s, reason -> message is illegal",
                topic,
                messageId,
                field,
                value
        ));
    }

    public IllegalMessageException(String topic, String messageId, String field) {
        super(String.format("topic -> %s, id -> %s, %s -> null || empty, reason -> message is illegal",
                topic,
                messageId,
                field
        ));
    }
}
