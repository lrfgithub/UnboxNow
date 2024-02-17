package com.unboxnow.common.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String className, int theId) {
        super(String.format("object -> %s, id -> %d, reason -> not found", className, theId));
    }

    public NotFoundException(String className, String field, String value) {
        super(String.format("object -> %s, %s -> %s, reason -> not found",
                className,
                field,
                value
        ));
    }

    public NotFoundException(String className, String field, int value) {
        super(String.format("object -> %s, %s -> %d, reason -> not found",
                className,
                field,
                value
        ));
    }
}
