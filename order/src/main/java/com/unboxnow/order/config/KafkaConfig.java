package com.unboxnow.order.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unboxnow.common.entity.CartItem;
import com.unboxnow.common.entity.QueryItem;
import com.unboxnow.common.entity.UniversalAddress;
import com.unboxnow.common.message.ConfirmationMessage;
import com.unboxnow.common.message.ContainerMessage;
import com.unboxnow.common.message.Message;
import com.unboxnow.common.message.RetrieverMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.listener.concurrency}")
    private Integer concurrency;

    @Bean(name = "kafkaTemplate")
    public KafkaTemplate<String, Message> kafkaTemplate(
            ProducerFactory<String, Message> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    private <T> ConsumerFactory<String, T> consumerFactory(TypeReference<T> typeReference) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructType(typeReference.getType());
        JsonDeserializer<T> jsonDeserializer = new JsonDeserializer<>(javaType, objectMapper);
        jsonDeserializer.addTrustedPackages("UnboxNow.order.common.message");
        jsonDeserializer.setUseTypeMapperForKey(true);
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                jsonDeserializer
        );
    }

    @Bean(name = "checkoutConsumerFactory")
    public ConsumerFactory<String, ContainerMessage<CartItem>> checkoutConsumerFactory() {
        TypeReference<ContainerMessage<CartItem>> typeReference = new TypeReference<>() {};
        return consumerFactory(typeReference);
    }

    @Bean(name = "checkoutListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, ContainerMessage<CartItem>> checkoutListenerFactory(
            ConsumerFactory<String, ContainerMessage<CartItem>> checkoutConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, ContainerMessage<CartItem>> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(checkoutConsumerFactory);
        factory.setConcurrency(concurrency);
        return factory;
    }

    @Bean(name = "lockConsumerFactory")
    public ConsumerFactory<String, ConfirmationMessage<QueryItem>> lockConsumerFactory() {
        TypeReference<ConfirmationMessage<QueryItem>> typeReference = new TypeReference<>() {};
        return consumerFactory(typeReference);
    }

    @Bean(name = "lockListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, ConfirmationMessage<QueryItem>> lockListenerFactory(
            ConsumerFactory<String, ConfirmationMessage<QueryItem>> lockConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, ConfirmationMessage<QueryItem>> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(lockConsumerFactory);
        factory.setConcurrency(concurrency);
        return factory;
    }

    @Bean(name = "unlockingConsumerFactory")
    public ConsumerFactory<String, Message> unlockingConsumerFactory() {
        TypeReference<Message> typeReference = new TypeReference<>() {};
        return consumerFactory(typeReference);
    }

    @Bean(name = "unlockingListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Message> unlockingListenerFactory(
            ConsumerFactory<String, Message> unlockingConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, Message> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(unlockingConsumerFactory);
        factory.setConcurrency(concurrency);
        return factory;
    }

    @Bean(name = "shipmentConsumerFactory")
    public ConsumerFactory<String, RetrieverMessage<Boolean>> shipmentConsumerFactory() {
        TypeReference<RetrieverMessage<Boolean>> typeReference = new TypeReference<>() {};
        return consumerFactory(typeReference);
    }

    @Bean(name = "shipmentListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, RetrieverMessage<Boolean>> shipmentListenerFactory(
            ConsumerFactory<String, RetrieverMessage<Boolean>> shipmentConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, RetrieverMessage<Boolean>> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(shipmentConsumerFactory);
        factory.setConcurrency(concurrency);
        return factory;
    }

    @Bean(name = "addressConsumerFactory")
    public ConsumerFactory<String, RetrieverMessage<UniversalAddress>> addressConsumerFactory() {
        TypeReference<RetrieverMessage<UniversalAddress>> typeReference = new TypeReference<>() {};
        return consumerFactory(typeReference);
    }

    @Bean(name = "addressListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, RetrieverMessage<UniversalAddress>> addressListenerFactory(
            ConsumerFactory<String, RetrieverMessage<UniversalAddress>> addressConsumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, RetrieverMessage<UniversalAddress>> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(addressConsumerFactory);
        factory.setConcurrency(concurrency);
        return factory;
    }

}
