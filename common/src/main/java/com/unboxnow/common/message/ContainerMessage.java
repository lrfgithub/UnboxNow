package com.unboxnow.common.message;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public class ContainerMessage<T> extends Message {

    @NotEmpty
    @Valid
    private List<T> items;

    public ContainerMessage() {
    }

    public ContainerMessage(Integer entityId) {
        super(entityId);
    }

    public ContainerMessage(String id, String type, Integer entityId) {
        super(id, type, entityId);
    }

    public ContainerMessage(Integer entityId, List<T> items) {
        super(entityId);
        this.items = items;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public void add(T t) {
        if (t == null) return;
        if (items == null) this.items = new ArrayList<>();
        this.items.add(t);
    }

    @Override
    public String toString() {
        return "ContainerMessage{" +
                "id='" + getId() + '\'' +
                ", type='" + getType() + '\'' +
                ", entityId=" + getEntityId() +
                ", items=" + items +
                '}';
    }
}
