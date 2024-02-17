package com.unboxnow.common.exception;

public class CustomHttpRequestException extends RuntimeException {

    public CustomHttpRequestException(String type, String msg) {
        super(String.format("object -> HttpRequest, type -> %s, msg -> %s", type, msg));
    }
}
