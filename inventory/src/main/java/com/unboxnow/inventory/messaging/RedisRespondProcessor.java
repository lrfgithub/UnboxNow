package com.unboxnow.inventory.messaging;

import com.unboxnow.common.constant.Topic;
import com.unboxnow.common.entity.QueryItem;
import com.unboxnow.common.message.ConfirmationMessage;
import com.unboxnow.common.message.ContainerMessage;
import com.unboxnow.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RedisRespondProcessor {

    private final ThreadPoolTaskExecutor threadTaskExecutor;

    private final InventoryService inventoryService;

    private final Producer producer;

    @Autowired
    public RedisRespondProcessor(ThreadPoolTaskExecutor threadTaskExecutor,
                                 InventoryService inventoryService,
                                 Producer producer) {
        this.threadTaskExecutor = threadTaskExecutor;
        this.inventoryService = inventoryService;
        this.producer = producer;
    }

    public void checkQuantity(ContainerMessage<QueryItem> request){
        threadTaskExecutor.submit(() -> {
            ConfirmationMessage<QueryItem> response = new ConfirmationMessage<>(request.getEntityId());
            Map<String, Integer> skuQuantityMap = new HashMap<>();
            Map<String, List<QueryItem>> skuItemMap = new HashMap<>();
            request.getItems().forEach(item -> {
                String sku = item.getSku();
                skuQuantityMap.put(sku, skuQuantityMap.getOrDefault(sku, 0) + item.getQuantity());
                if (!skuItemMap.containsKey(sku)) {
                    skuItemMap.put(sku, new ArrayList<>());
                }
                skuItemMap.get(sku).add(item);
            });
            skuQuantityMap.keySet().forEach(sku -> {
                if (!inventoryService.checkQuantityBySku(sku, skuQuantityMap.get(sku))) {
                    skuItemMap.get(sku).forEach(response::add);
                }
            });
            response.setValid(response.getItems() == null || response.getItems().isEmpty());
            producer.publish(response, Topic.CHECK_QUANTITY, request.getId());
        });
    }
}
