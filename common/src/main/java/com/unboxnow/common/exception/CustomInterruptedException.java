package com.unboxnow.common.exception;

public class CustomInterruptedException extends RuntimeException {

    public CustomInterruptedException(String key, String hashKey) {
        super(String.format("Type -> Hash, key -> %s, hashKey -> %s, reason -> thread is interrupted",
                key,
                hashKey
        ));
    }

    public CustomInterruptedException(String key, int hashKey) {
        super(String.format("Type -> Hash, key -> %s, hashKey -> %d, reason -> thread is interrupted",
                key,
                hashKey
        ));
    }
}
