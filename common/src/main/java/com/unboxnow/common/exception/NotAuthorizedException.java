package com.unboxnow.common.exception;

public class NotAuthorizedException extends RuntimeException{

    public NotAuthorizedException(int memberId) {
        super(String.format("memberId -> %d, reason -> not authorized", memberId));
    }
}
