package com.unboxnow.inventory.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unboxnow.common.entity.QueryItem;
import com.unboxnow.common.exception.JsonException;
import com.unboxnow.common.message.ContainerMessage;
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
    public void checkQuantity(String requestStr) {
        ContainerMessage<QueryItem> request;
        TypeReference<ContainerMessage<QueryItem>> reference = new TypeReference<>() {};
        try {
            request = mapper.readValue(requestStr, reference);
        } catch (JsonProcessingException ex) {
            throw new JsonException("ContainerMessage<QueryItem>", ex.getMessage());
        }
        redisRespondProcessor.checkQuantity(request);
    }

//    public <T> List<String> validateMessage(T t) {
//        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
//        Set<ConstraintViolation<T>> errs = validator.validate(t);
//        return errs.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
//    }
}
