package com.unboxnow.order.messaging;

import com.unboxnow.common.entity.CartItem;
import com.unboxnow.common.entity.QueryItem;
import com.unboxnow.common.entity.UniversalAddress;
import com.unboxnow.common.message.ConfirmationMessage;
import com.unboxnow.common.message.ContainerMessage;
import com.unboxnow.common.message.Message;
import com.unboxnow.common.message.RetrieverMessage;
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

    @KafkaListener(id = "CheckOut-Order-01",
            topicPartitions = {@TopicPartition(topic = "CART_TO_ORDER_REQUEST", partitions = {"0"})},
            containerFactory = "checkoutListenerFactory")
    public void receivePreorder(ConsumerRecord<String, ContainerMessage<CartItem>> record) {
        kafkaRequestProcessor.receivePreorder(record.value());
    }

    @KafkaListener(id = "FetchAddress-Order-01",
            topicPartitions = {@TopicPartition(topic = "USER_TO_ORDER_RESPONSE", partitions = {"0"})},
            containerFactory = "addressListenerFactory")
    public void receiveAddress(ConsumerRecord<String, RetrieverMessage<UniversalAddress>> record) {
        kafkaRequestProcessor.receiveAddress(record.value());
    }

    @KafkaListener(id = "LockQuantity-Order-01",
            topicPartitions = {@TopicPartition(topic = "INVENTORY_TO_ORDER_LOCK_RESPONSE", partitions = {"0"})},
            containerFactory = "lockListenerFactory")
    public void receiveLock(ConsumerRecord<String, ConfirmationMessage<QueryItem>> record) {
        kafkaRequestProcessor.receiveLock(record.value());
    }

    @KafkaListener(id = "StopLocking-Order-01",
            topicPartitions = {@TopicPartition(topic = "INVENTORY_TO_ORDER_UPDATE", partitions = {"0"})},
            containerFactory = "unlockingListenerFactory")
    public void unlock(ConsumerRecord<String, Message> record) {
        kafkaRequestProcessor.unlock(record.value());
    }

    @KafkaListener(id = "StartShipment-Order-01",
            topicPartitions = {@TopicPartition(topic = "INVENTORY_TO_ORDER_SHIPMENT_RESPONSE", partitions = {"0"})},
            containerFactory = "shipmentListenerFactory")
    public void startShipment(ConsumerRecord<String, RetrieverMessage<Boolean>> record) {
        kafkaRequestProcessor.startShipment(record.value());
    }
}
