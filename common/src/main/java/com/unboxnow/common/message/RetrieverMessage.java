package com.unboxnow.common.message;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class RetrieverMessage<T> extends Message {

    @NotNull
    private Boolean valid;

    @Valid
    private T data;

    public RetrieverMessage() {
    }

    public RetrieverMessage(Integer entityId) {
        super(entityId);
    }

    public RetrieverMessage(Integer entityId, Boolean valid, T data) {
        super(entityId);
        this.valid = valid;
        this.data = data;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RetrieveMessage{" +
                "id='" + getId() + '\'' +
                ", type='" + getType() + '\'' +
                ", entityId=" + getEntityId() +
                ", valid=" + valid +
                ", data=" + data +
                '}';
    }
}
