package com.unboxnow.common.exception;

import com.unboxnow.common.constant.Topic;

public class IllegalValueException extends RuntimeException {

    public IllegalValueException(String key, String hashKey, String value) {
        super(String.format("Type -> Hash, key -> %s, hashKey -> %s, value -> %s, reason -> value is illegal",
                key,
                hashKey,
                value
        ));
    }

    public IllegalValueException(String key, int hashKey, String value) {
        super(String.format("Type -> Hash, key -> %s, hashKey -> %d, value -> %s, reason -> value is illegal",
                key,
                hashKey,
                value
        ));
    }

    public IllegalValueException(String key, String hashKey) {
        super(String.format("Type -> Hash, key -> %s, hashKey -> %s, value -> null, reason -> value is illegal",
                key,
                hashKey
        ));
    }

    public IllegalValueException(String key, int hashKey) {
        super(String.format("Type -> Hash, key -> %s, hashKey -> %d, value -> null, reason -> value is illegal",
                key,
                hashKey
        ));
    }

    /*
        for Lock Update in inventory
     */
    public IllegalValueException() {
        super(String.format("Type -> ZSet, key -> %s, value -> null, reason -> value is illegal",
                Topic.START_LOCKING.getName()
        ));
    }

    public IllegalValueException(String field) {
        super(String.format("Type -> ZSet, key -> %s, %s -> null, reason -> value is illegal",
                Topic.START_LOCKING.getName(),
                field
        ));
    }
}
