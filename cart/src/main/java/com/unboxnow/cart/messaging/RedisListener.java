package com.unboxnow.cart.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unboxnow.common.entity.ProductItem;
import com.unboxnow.common.entity.QueryItem;
import com.unboxnow.common.exception.JsonException;
import com.unboxnow.common.message.ConfirmationMessage;
import com.unboxnow.common.message.Message;
import com.unboxnow.common.message.RetrieverMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class RedisListener {

    private final RedisRespondProcessor redisRespondProcessor;

    private final ObjectMapper mapper;

    @Autowired
    public RedisListener(RedisRespondProcessor redisRespondProcessor) {
        this.redisRespondProcessor = redisRespondProcessor;
        mapper = new ObjectMapper();
    }

    @Async
    public void saveProduct(String responseStr){
        TypeReference<RetrieverMessage<ProductItem>> reference =  new TypeReference<>() {};
        RetrieverMessage<ProductItem> response;
        try {
            response = mapper.readValue(responseStr, reference);
        } catch (JsonProcessingException ex) {
            throw new JsonException("RetrieverMessage<ProductItem>", ex.getMessage());
        }
        redisRespondProcessor.saveProduct(response);
    }

    @Async
    public void updateProduct(String updateStr){
        Message update;
        try {
            update = mapper.readValue(updateStr, Message.class);
        } catch (JsonProcessingException ex) {
            throw new JsonException("Message", ex.getMessage());
        }
        redisRespondProcessor.updateProduct(update);
    }

    @Async
    public void saveMember(String responseStr){
        TypeReference<RetrieverMessage<Boolean>> reference =  new TypeReference<>() {};
        RetrieverMessage<Boolean> response;
        try {
            response = mapper.readValue(responseStr, reference);
        } catch (JsonProcessingException ex) {
            throw new JsonException("RetrieverMessage<Boolean>", ex.getMessage());
        }
        redisRespondProcessor.saveMember(response);
    }

    @Async
    public void updateMember(String updateStr){
        Message update;
        try {
            update = mapper.readValue(updateStr, Message.class);
        } catch (JsonProcessingException ex) {
            throw new JsonException("Message", ex.getMessage());
        }
        redisRespondProcessor.updateMember(update);
    }

    @Async
    public void saveInventory(String responseStr) {
        TypeReference<ConfirmationMessage<QueryItem>> reference =  new TypeReference<>() {};
        ConfirmationMessage<QueryItem> response;
        try {
            response = mapper.readValue(responseStr, reference);
        } catch (JsonProcessingException ex) {
            throw new JsonException("ConfirmationMessage<QueryItem>", ex.getMessage());
        }
        redisRespondProcessor.saveInventory(response);
    }
}
