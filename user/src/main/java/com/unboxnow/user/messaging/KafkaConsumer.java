package com.unboxnow.user.messaging;

import com.unboxnow.common.message.CarrierMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private final KafkaRequestProcessor kafkaRequestProcessor;

    @Autowired
    public KafkaConsumer(KafkaRequestProcessor kafkaRequestProcessor) {
        this.kafkaRequestProcessor = kafkaRequestProcessor;
    }

    @KafkaListener(id = "FetchAddress-User-01",
            topicPartitions = {@TopicPartition(topic = "ORDER_TO_USER_REQUEST", partitions = {"0"})},
            containerFactory = "listenerFactory")
    public void getAddress(ConsumerRecord<String, CarrierMessage<Integer>> record) {
        kafkaRequestProcessor.getAddress(record.value());
    }
}
