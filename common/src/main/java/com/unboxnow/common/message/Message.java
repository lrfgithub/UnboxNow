package com.unboxnow.common.message;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class Message implements Serializable {

    @NotBlank
    private String id;

    @NotBlank
    private String type;

    @NotNull
    @Min(1)
    private Integer entityId;

    public Message() {
    }

    public Message(Integer entityId) {
        this.entityId = entityId;
    }

    public Message(String id, String type, Integer entityId) {
        this.id = id;
        this.type = type;
        this.entityId = entityId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", entityId=" + entityId +
                '}';
    }
}
