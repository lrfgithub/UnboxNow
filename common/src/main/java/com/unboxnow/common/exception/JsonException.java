package com.unboxnow.common.exception;

public class JsonException extends RuntimeException {

    public JsonException(String classname, String msg) {
        super(String.format("object -> %s msg -> %s", classname, msg));
    }
}
