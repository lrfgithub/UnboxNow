package com.unboxnow.cart.messaging;

import com.unboxnow.cart.constant.EntityNames;
import com.unboxnow.cart.service.CartService;
import com.unboxnow.common.constant.Topic;
import com.unboxnow.common.entity.ProductItem;
import com.unboxnow.common.entity.QueryItem;
import com.unboxnow.common.exception.IllegalMessageException;
import com.unboxnow.common.message.ConfirmationMessage;
import com.unboxnow.common.message.Message;
import com.unboxnow.common.message.RetrieverMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class RedisRespondProcessor {

    private final ThreadPoolTaskExecutor threadTaskExecutor;

    private final CartService cartService;

    private final HashOperations<String, Integer, ProductItem> productOps;

    private final HashOperations<String, String, Boolean> memberOps;

    private final HashOperations<String, Integer, Boolean> inventoryOps;

    @Autowired
    public RedisRespondProcessor(ThreadPoolTaskExecutor threadTaskExecutor,
                                 CartService cartService,
                                 RedisTemplate<String, String> productRedisTemplate,
                                 RedisTemplate<String, String> memberRedisTemplate,
                                 RedisTemplate<String, String> inventoryRedisTemplate) {
        this.threadTaskExecutor = threadTaskExecutor;
        this.cartService = cartService;
        this.productOps = productRedisTemplate.opsForHash();
        this.memberOps = memberRedisTemplate.opsForHash();
        this.inventoryOps = inventoryRedisTemplate.opsForHash();
    }

    public void saveProduct(RetrieverMessage<ProductItem> response) {
        threadTaskExecutor.submit(() -> {
            int productId = response.getEntityId();
            if (!response.getValid()) {
                productOps.delete(EntityNames.PRODUCT, productId);
                return;
            } else if (response.getData() == null) {
                throw new IllegalMessageException(Topic.FETCH_PRODUCT.getResponseName(), response.getId(), "data");
            }
            productOps.put(EntityNames.PRODUCT, productId, response.getData());
        });
    }

    public void updateProduct(Message update) {
        threadTaskExecutor.submit(() -> {
            productOps.delete(EntityNames.PRODUCT, update.getEntityId());
        });
    }

    public void saveMember(RetrieverMessage<Boolean> response) {
        threadTaskExecutor.submit(() -> {
            String id = response.getId();
            boolean value = response.getData() != null && response.getData();
            memberOps.put(EntityNames.MEMBER, id, value);
        });
    }

    public void updateMember(Message update) {
        threadTaskExecutor.submit(() -> cartService.deleteItemsByMemberId(update.getEntityId()));
    }

    public void saveInventory(ConfirmationMessage<QueryItem> response) {
        threadTaskExecutor.submit(() -> {
            String key = EntityNames.getInventoryKey(response.getId());
            if (response.getValid()) {
                inventoryOps.put(key, 0, true);
            } else {
                inventoryOps.put(key, 0, false);
                if (response.getItems() == null || response.getItems().isEmpty()) {
                    throw new IllegalMessageException(Topic.CHECK_QUANTITY.getResponseName(),
                            response.getId(),
                            "items");
                }
                response.getItems().forEach(item -> inventoryOps.put(key, item.getProductId(), false));
            }
        });
    }

}
