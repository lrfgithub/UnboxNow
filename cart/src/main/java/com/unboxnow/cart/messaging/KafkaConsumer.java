package com.unboxnow.cart.messaging;

import com.unboxnow.common.entity.CartItem;
import com.unboxnow.common.message.ConfirmationMessage;
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

    @KafkaListener(id = "CheckOut-Cart-01",
            topicPartitions = {@TopicPartition(topic = "ORDER_TO_CART_RESPONSE", partitions = {"0"})},
            containerFactory = "checkoutListenerFactory")
    public void clearCart(ConsumerRecord<String, ConfirmationMessage<CartItem>> record) {
        kafkaRequestProcessor.clearCart(record.value());
    }
}
