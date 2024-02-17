package com.unboxnow.user.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unboxnow.common.exception.JsonException;
import com.unboxnow.common.message.Message;
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
        this.mapper = new ObjectMapper();
    }

    @Async
    public void getMember(String requestStr) {
        Message request;
        try {
            request = mapper.readValue(requestStr, Message.class);
        } catch (JsonProcessingException ex) {
            throw new JsonException("Message", ex.getMessage());
        }
        redisRespondProcessor.getMember(request);
    }
}
