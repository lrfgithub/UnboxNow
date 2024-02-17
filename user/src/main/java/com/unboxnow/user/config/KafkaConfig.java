package com.unboxnow.user.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unboxnow.common.message.CarrierMessage;
import com.unboxnow.common.message.Message;
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
        jsonDeserializer.addTrustedPackages("UnboxNow.user.common.message");
        jsonDeserializer.setUseTypeMapperForKey(true);
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                jsonDeserializer
        );
    }

    @Bean(name = "consumerFactory")
    public ConsumerFactory<String, CarrierMessage<Integer>> consumerFactory() {
        TypeReference<CarrierMessage<Integer>> typeReference = new TypeReference<>() {};
        return consumerFactory(typeReference);
    }

    @Bean(name = "listenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, CarrierMessage<Integer>> listenerFactory(
            ConsumerFactory<String, CarrierMessage<Integer>> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, CarrierMessage<Integer>> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(concurrency);
        return factory;
    }
}
