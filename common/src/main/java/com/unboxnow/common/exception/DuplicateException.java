package com.unboxnow.common.exception;

public class DuplicateException extends RuntimeException {

    public DuplicateException(String className, String field, String value) {
        super(String.format("object -> %s, %s -> %s, reason -> duplicate value",
                className,
                field,
                value
        ));
    }
}
