package com.unboxnow.common.exception;

public class ActiveDiscountException extends RuntimeException {

    public ActiveDiscountException(int theId) {
        super(String.format("object -> Discount, id -> %d, reason -> still active", theId));
    }
}
