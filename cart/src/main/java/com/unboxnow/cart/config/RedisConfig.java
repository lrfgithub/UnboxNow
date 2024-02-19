package com.unboxnow.cart.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unboxnow.cart.messaging.RedisListener;
import com.unboxnow.common.constant.Topic;
import com.unboxnow.common.entity.ProductItem;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Objects;

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

    /*
    cart template
    |           | type    | content   | format               | e.g.      |
    | key       | String  | memberId  | memberId: <memberId> | member: 1 |
    | hashKey   | Integer | productId | <productId>          | 1         |
    | hashValue | Integer | quantity  | <quantity>           | 1         |
    */
    @Bean(name = "cartRedisTemplate")
    public RedisTemplate<String, String> cartRedisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new Jackson2JsonRedisSerializer<>(Integer.class));
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Integer.class));

        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    /*
    product template
    |           | type        | content        | format        | e.g.    |
    | key       | String      | product        | product       | product |
    | hashKey   | Integer     | productId      | <productId>   | 1       |
    | hashValue | ProductItem | product detail | <ProductItem> |         |
    */
    @Bean(name = "productRedisTemplate")
    public RedisTemplate<String, String> productRedisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();

        ObjectMapper objectMapper = new ObjectMapper();
        Jackson2JsonRedisSerializer<ProductItem> jackson2JsonRedisSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, ProductItem.class);

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new Jackson2JsonRedisSerializer<>(Integer.class));
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    /*
    member template
    |           | type    | content      | format      | e.g.   |
    | key       | String  | member       | member      | member |
    | hashKey   | String  | messageId    | <messageId> | 1      |
    | hashValue | Boolean | valid or not | <valid>     | true   |
    */
    @Bean(name = "memberRedisTemplate")
    public RedisTemplate<String, String> memberRedisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Boolean.class));

        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    /*
    inventory template
    |           | type    | content               | format                 | e.g.         |
    | key       | String  | messageId             | inventory: <messageId> | inventory: 1 |
    | hashKey   | Integer | productId             | <productId>            | 1            |
    | hashValue | Boolean | Availability of stock | <Availability>         | false        |
    */
    @Bean(name = "inventoryRedisTemplate")
    public RedisTemplate<String, String> inventoryRedisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new Jackson2JsonRedisSerializer<>(Integer.class));
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Boolean.class));

        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean(name = "productCheckAdapter")
    public MessageListenerAdapter productCheckAdapter(RedisListener redisListener){
        return new MessageListenerAdapter(redisListener, "saveProduct");
    }

    @Bean(name = "productUpdateAdapter")
    public MessageListenerAdapter productUpdateAdapter(RedisListener redisListener){
        return new MessageListenerAdapter(redisListener, "updateProduct");
    }

    @Bean(name = "memberCheckAdapter")
    public MessageListenerAdapter memberCheckAdapter(RedisListener redisListener){
        return new MessageListenerAdapter(redisListener, "saveMember");
    }

    @Bean(name = "memberUpdateAdapter")
    public MessageListenerAdapter memberUpdateAdapter(RedisListener redisListener){
        return new MessageListenerAdapter(redisListener, "updateMember");
    }

    @Bean(name = "inventoryCheckAdapter")
    public MessageListenerAdapter inventoryCheckAdapter(RedisListener redisListener){
        return new MessageListenerAdapter(redisListener, "saveInventory");
    }

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   @Qualifier("productCheckAdapter") MessageListenerAdapter productCheckAdapter,
                                                   @Qualifier("productUpdateAdapter") MessageListenerAdapter productUpdateAdapter,
                                                   @Qualifier("memberCheckAdapter") MessageListenerAdapter memberCheckAdapter,
                                                   @Qualifier("memberUpdateAdapter") MessageListenerAdapter memberUpdateAdapter,
                                                   @Qualifier("inventoryCheckAdapter") MessageListenerAdapter inventoryCheckAdapter){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(productCheckAdapter, new PatternTopic(
                Objects.requireNonNull(Topic.FETCH_PRODUCT.getResponseName())));
        container.addMessageListener(productUpdateAdapter, new PatternTopic(Topic.UPDATE_PRODUCT.getName()));
        container.addMessageListener(memberCheckAdapter, new PatternTopic(
                Objects.requireNonNull(Topic.FETCH_MEMBER.getResponseName())));
        container.addMessageListener(memberUpdateAdapter, new PatternTopic(Topic.UPDATE_MEMBER.getName()));
        container.addMessageListener(inventoryCheckAdapter, new PatternTopic(
                Objects.requireNonNull(Topic.CHECK_QUANTITY.getResponseName())));
        return container;
    }
}
