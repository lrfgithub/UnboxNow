package com.unboxnow.common.exception;

public class MinQuantityException extends RuntimeException {

    public MinQuantityException(String className, int quantity, int minimum) {
        super(String.format("object -> %s, quantity -> %d, reason -> quantity must be greater than or equal to %d",
                className,
                quantity,
                minimum
        ));
    }
}
