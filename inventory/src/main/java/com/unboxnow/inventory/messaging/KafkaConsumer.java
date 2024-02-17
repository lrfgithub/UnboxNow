package com.unboxnow.inventory.messaging;

import com.unboxnow.common.entity.QueryItem;
import com.unboxnow.common.entity.UniversalAddress;
import com.unboxnow.common.message.CarrierMessage;
import com.unboxnow.common.message.ContainerMessage;
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

    @KafkaListener(id = "LockQuantity-Inventory-01",
            topicPartitions = {@TopicPartition(topic = "ORDER_TO_INVENTORY_LOCK_REQUEST", partitions = {"0"})},
            containerFactory = "lockListenerFactory")
    public void createQuantityLock(ConsumerRecord<String, ContainerMessage<QueryItem>> record) {
        kafkaRequestProcessor.createLock(record.value());
    }

    @KafkaListener(id = "StartShipment-Inventory-01",
            topicPartitions = {@TopicPartition(topic = "ORDER_TO_INVENTORY_SHIPMENT_REQUEST", partitions = {"0"})},
            containerFactory = "shipmentListenerFactory")
    public void createShipments(ConsumerRecord<String, CarrierMessage<UniversalAddress>> record) {
        kafkaRequestProcessor.createShipments(record.value());
    }
}
