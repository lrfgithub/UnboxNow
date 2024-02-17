package com.unboxnow.common.exception;

public class MaxQuantityException extends RuntimeException {

    public MaxQuantityException(String className, int quantity) {
        super(String.format("object -> %s, quantity -> %d, reason -> insufficient products in stock",
                className,
                quantity
        ));
    }
}
