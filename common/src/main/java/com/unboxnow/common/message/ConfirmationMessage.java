package com.unboxnow.common.message;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ConfirmationMessage<T> extends Message {

    @NotNull
    private Boolean valid;

    @Valid
    private List<T> items;

    public ConfirmationMessage() {
    }

    public ConfirmationMessage(Integer entityId) {
        super(entityId);
    }

    public ConfirmationMessage(Integer entityId, Boolean valid) {
        super(entityId);
        this.valid = valid;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public void add(T t) {
        if (t == null) return;
        if (getItems() == null) items = new ArrayList<>();
        getItems().add(t);
    }

    @Override
    public String toString() {
        return "ContainerMessage{" +
                "id='" + getId() + '\'' +
                ", type='" + getType() + '\'' +
                ", entityId=" + getEntityId() +
                ", items=" + getItems() +
                ", valid=" + valid +
                '}';
    }
}
