package com.unboxnow.cart.service;

import com.unboxnow.cart.constant.EntityNames;
import com.unboxnow.cart.dto.CartDTO;
import com.unboxnow.cart.dto.CartItemDTO;
import com.unboxnow.cart.messaging.Producer;
import com.unboxnow.common.constant.Topic;
import com.unboxnow.common.entity.CartItem;
import com.unboxnow.common.entity.ProductItem;
import com.unboxnow.common.entity.QueryItem;
import com.unboxnow.common.exception.CustomInterruptedException;
import com.unboxnow.common.exception.IllegalValueException;
import com.unboxnow.common.exception.MinQuantityException;
import com.unboxnow.common.exception.NotFoundException;
import com.unboxnow.common.message.ContainerMessage;
import com.unboxnow.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CartServiceImpl implements CartService {

    private final HashOperations<String, Integer, Integer> cartOps;

    private final HashOperations<String, Integer, ProductItem> productOps;

    private final HashOperations<String, String, Boolean> memberOps;

    private final HashOperations<String, Integer, Boolean> inventoryOps;

    private final Producer producer;

    @Autowired
    public CartServiceImpl(RedisTemplate<String, String> cartRedisTemplate,
                           RedisTemplate<String, String> productRedisTemplate,
                           RedisTemplate<String, String> memberRedisTemplate,
                           RedisTemplate<String, String> inventoryRedisTemplate,
                           Producer producer) {
        this.cartOps = cartRedisTemplate.opsForHash();
        this.productOps = productRedisTemplate.opsForHash();
        this.memberOps = memberRedisTemplate.opsForHash();
        this.inventoryOps = inventoryRedisTemplate.opsForHash();
        this.producer = producer;
    }

    @Override
    public CartDTO findByMemberId(int memberId) {
        String key = EntityNames.getCartKey(memberId);
        Set<Integer> productIds = cartOps.keys(key);
        productIds.forEach(this::requestProductIfAbsent);
        CartDTO dto = new CartDTO(memberId);
        for (int productId : productIds) {
            ProductItem productItem = getProductResponse(productId);
            if (productItem == null) {
                cartOps.delete(key, productId);
            } else {
                CartItem item = CartItem.fromProductItem(productItem, cartOps.get(key, productId));
                dto.add(item);
            }
        }
        dto.update();
        return dto;
    }

    @Override
    public CartDTO add(CartItemDTO dto) {
        int memberId = dto.getMemberId();
        String key = EntityNames.getCartKey(memberId);
        if (cartOps.keys(key).isEmpty()) {
            String memberRequestId = producer.publish(new Message(memberId), Topic.FETCH_MEMBER);
            if (!getMemberResponse(memberRequestId)) {
                throw new NotFoundException("Member", memberId);
            }
        }
        cartOps.putIfAbsent(key, dto.getProductId(), 0);
        cartOps.increment(key, dto.getProductId(), dto.getQuantity());
        return findByMemberId(memberId);
    }

    @Override
    public CartDTO subtract(CartItemDTO dto) {
        int memberId = dto.getMemberId();
        String key = EntityNames.getCartKey(memberId);
        int productId = dto.getProductId(), quantity = dto.getQuantity();
        if (!cartOps.hasKey(key, productId)) {
            throw new NotFoundException("Cart", "productId", productId);
        }
        Integer oldQuantity = cartOps.get(key, productId);
        if (oldQuantity == null || oldQuantity < 1) {
            cartOps.delete(key, productId);
            throw new IllegalValueException(key, productId, oldQuantity == null? "null" : oldQuantity.toString());
        }
        int newQuantity = oldQuantity - quantity;
        if (newQuantity < 1) {
            throw new MinQuantityException("Cart", newQuantity, 1);
        }
        cartOps.put(key, productId, newQuantity);
        return findByMemberId(memberId);
    }

    @Override
    public void deleteItem(CartItemDTO dto) {
        int memberId = dto.getMemberId();
        int productId = dto.getProductId();
        String key = EntityNames.getCartKey(memberId);
        if (cartOps.hasKey(key, productId)) {
            cartOps.delete(key, productId);
        } else {
            throw new NotFoundException("Cart", "productId", productId);
        }
    }

    @Override
    public void deleteItemsByMemberId(int memberId) {
        String key = EntityNames.getCartKey(memberId);
        Set<Integer> productIds = cartOps.keys(key);
        productIds.forEach(productId -> cartOps.delete(key, productId));
    }

    @Override
    public List<CartItem> placeOrder(int memberId) {
        String key = EntityNames.getCartKey(memberId);
        Set<Integer> productIds = cartOps.keys(key);
        productIds.forEach(this::requestProductIfAbsent);
        ContainerMessage<QueryItem> quantityCheckRequest = new ContainerMessage<>(memberId);
        ContainerMessage<CartItem> checkoutRequest = new ContainerMessage<>(memberId);
        for (int productId : productIds) {
            ProductItem productItem = getProductResponse(productId);
            if (productItem == null) {
                cartOps.delete(key, productId);
                continue;
            }
            Integer quantity = cartOps.get(key, productId);
            if (quantity == null || quantity < 1) {
                cartOps.delete(key, productId);
                continue;
            }
            quantityCheckRequest.add(QueryItem.fromProductItem(productItem, quantity));
            checkoutRequest.add(CartItem.fromProductItem(productItem, quantity));
        }
        String quantityCheckRequestId = producer.publish(quantityCheckRequest, Topic.CHECK_QUANTITY);
        String inventoryKey = EntityNames.getInventoryKey(quantityCheckRequestId);
        List<CartItem> outOfStockItems = new ArrayList<>();
        if (!getInventoryResponse(inventoryKey)) {
            Set<Integer> outOfStockIds = inventoryOps.keys(inventoryKey);
            for (CartItem cartItem : checkoutRequest.getItems()) {
                if (outOfStockIds.contains(cartItem.getProductId())) {
                    outOfStockItems.add(cartItem);
                }
            }
        } else {
            producer.publish(checkoutRequest, Topic.CHECK_OUT);
        }
        return outOfStockItems;
    }

    @Override
    public CartDTO clear(int memberId, Set<Integer> stockOutProductIds) {
        String key = EntityNames.getCartKey(memberId);
        Set<Integer> productIds = cartOps.keys(key);
        productIds.forEach(id -> {
            if (!stockOutProductIds.contains(id)) {
                cartOps.delete(key, id);
            }
        });
        return findByMemberId(memberId);
    }

    private void requestProductIfAbsent(int productId) {
        if (productOps.hasKey(EntityNames.PRODUCT, productId)) return;
        producer.publish(new Message(productId), Topic.FETCH_PRODUCT);
    }

    private ProductItem getProductResponse(int productId) {
        int attempts = 20, interval = 25;
        try {
            for (int i = 0; i < attempts; i++) {
                if (productOps.hasKey(EntityNames.PRODUCT, productId)) {
                    return productOps.get(EntityNames.PRODUCT, productId);
                }
                Thread.sleep(interval);
            }
        } catch (InterruptedException ex) {
            throw new CustomInterruptedException(EntityNames.PRODUCT, productId);
        }
        return null;
    }

    private Boolean getMemberResponse(String messageId) {
        int attempts = 20, interval = 25;
        Boolean res = null;
        try {
            for (int i = 0; i < attempts; i++) {
                if (memberOps.hasKey(EntityNames.MEMBER, messageId)) {
                    res = memberOps.get(EntityNames.MEMBER, messageId);
                    memberOps.delete(EntityNames.MEMBER, messageId);
                    break;
                }
                Thread.sleep(interval);
            }
        } catch (InterruptedException ex) {
            throw new CustomInterruptedException(EntityNames.MEMBER, messageId);
        }
        if (res == null) {
            throw new IllegalValueException(EntityNames.MEMBER, messageId);
        }
        return res;
    }

    private Boolean getInventoryResponse(String key) {
        int attempts = 20, interval = 25;
        Boolean res = null;
        try {
            for (int i = 0; i < attempts; i++) {
                if (inventoryOps.hasKey(key, 0)) {
                    res = inventoryOps.get(key, 0);
                    inventoryOps.delete(key, 0);
                    break;
                }
                Thread.sleep(interval);
            }
        } catch (InterruptedException ex) {
            throw new CustomInterruptedException(key, 0);
        }
        if (res == null) {
            throw new IllegalValueException(key, 0);
        }
        return res;
    }
}
