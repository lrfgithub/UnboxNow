package com.unboxnow.order.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unboxnow.common.entity.CartItem;
import com.unboxnow.common.message.ContainerMessage;
import com.unboxnow.order.dto.OrderDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    /*
    preorder template
    |           | type                       | content           | format                       | e.g.  |
    | key       | String                     | order             | order                        | order |
    | hashKey   | Integer                    | memberId          | <memberId>                   | 1     |
    | hashValue | ContainerMessage<CartItem> | request from cart | <ContainerMessage<CartItem>> |       |
    */
    @Bean(name = "preorderRedisTemplate")
    public RedisTemplate<String, String> preorderRedisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<ContainerMessage<CartItem>> typeReference = new TypeReference<>() {};
        JavaType javaType = objectMapper.getTypeFactory().constructType(typeReference.getType());
        Jackson2JsonRedisSerializer<OrderDTO> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, javaType);

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new Jackson2JsonRedisSerializer<>(Integer.class));
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    /*
    order template
    |           | type    | content  | format     | e.g.  |
    | key       | String  | order    | order      | order |
    | hashKey   | Integer | memberId | <memberId> | 1     |
    | hashValue | Integer | orderId  | <orderId>  | 1     |
    */
    @Bean(name = "orderRedisTemplate")
    public RedisTemplate<String, String> orderRedisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();

        Jackson2JsonRedisSerializer<Integer> integerSerializer = new Jackson2JsonRedisSerializer<>(Integer.class);

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(integerSerializer);
        template.setHashValueSerializer(integerSerializer);

        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

}
