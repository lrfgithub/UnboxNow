package com.unboxnow.inventory.messaging;

import com.unboxnow.common.component.IdGenerator;
import com.unboxnow.common.constant.Topic;
import com.unboxnow.common.exception.PublishException;
import com.unboxnow.common.message.Message;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@ComponentScan(basePackages = {"com.unboxnow.common.component"})
public class Producer {

    private final RedisTemplate<String, Message> redisTemplate;

    private final KafkaTemplate<String, Message> kafkaTemplate;

    private final IdGenerator idGenerator;

    @Autowired
    public Producer(RedisTemplate<String, Message> redisTemplate,
                    KafkaTemplate<String, Message> kafkaTemplate,
                    IdGenerator idGenerator) {
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.idGenerator = idGenerator;
    }

    public void publish(@NotNull Message message, @NotNull Topic topic) {
        message.setId(idGenerator.getId(topic.getCounter()));
        message.setType(topic.getName());
        send(message, topic);
    }

    public void publish(@NotNull Message message, @NotNull Topic topic, @NotBlank String messageId) {
        message.setId(messageId);
        message.setType(topic.getResponseName());
        send(message, topic);
    }

    private void send(@Valid Message message, Topic topic) {
        try {
            if (topic.tool == Topic.TopicTool.REDIS) {
                redisTemplate.convertAndSend(message.getType(), message);
            } else {
                kafkaTemplate.send(message.getType(), message);
            }
        } catch (Exception ex) {
            throw new PublishException(message.getType(), ex.getMessage());
        }
    }
}
