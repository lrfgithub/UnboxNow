package com.unboxnow.common.message;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class CarrierMessage<T> extends Message {

    @NotNull
    @Valid
    private T data;

    public CarrierMessage() {
    }

    public CarrierMessage(Integer entityId, T data) {
        super(entityId);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CarrierMessage{" +
                "id='" + getId() + '\'' +
                ", type='" + getType() + '\'' +
                ", entityId=" + getEntityId() +
                ", data=" + data +
                '}';
    }
}
