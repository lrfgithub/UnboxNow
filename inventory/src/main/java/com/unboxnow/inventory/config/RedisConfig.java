package com.unboxnow.inventory.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unboxnow.common.constant.Topic;
import com.unboxnow.common.entity.InventoryItem;
import com.unboxnow.common.message.ContainerMessage;
import com.unboxnow.inventory.messaging.RedisListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean(name = "redisTemplate")
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<?, ?> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean(name = "lockRedisTemplate")
    public RedisTemplate<String, ContainerMessage<InventoryItem>> lockRedisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, ContainerMessage<InventoryItem>> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<ContainerMessage<InventoryItem>> typeReference = new TypeReference<>() {};
        JavaType javaType = objectMapper.getTypeFactory().constructType(typeReference.getType());
        Jackson2JsonRedisSerializer<ContainerMessage<InventoryItem>> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, javaType);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean(name = "inventoryAdapter")
    public MessageListenerAdapter inventoryAdapter(RedisListener redisListener){
        return new MessageListenerAdapter(redisListener, "checkQuantity");
    }

    @Bean(name = "redisMessageListenerContainer")
    public RedisMessageListenerContainer redisMessageListenerContainer
            (RedisConnectionFactory connectionFactory,
             MessageListenerAdapter inventoryAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(inventoryAdapter, new PatternTopic(Topic.CHECK_QUANTITY.getName()));
        return container;
    }
}
