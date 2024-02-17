package com.unboxnow.cart.messaging;

import com.unboxnow.cart.service.CartService;
import com.unboxnow.common.constant.Topic;
import com.unboxnow.common.entity.Item;
import com.unboxnow.common.entity.CartItem;
import com.unboxnow.common.exception.IllegalMessageException;
import com.unboxnow.common.message.ConfirmationMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class KafkaRequestProcessor {

    private final CartService cartService;

    @Autowired
    public KafkaRequestProcessor(CartService cartService) {
        this.cartService = cartService;
    }

    public void clearCart(ConfirmationMessage<CartItem> response) {
        int memberId = response.getEntityId();
        if (response.getValid()) {
            cartService.deleteItemsByMemberId(memberId);
        } else {
            List<CartItem> items = response.getItems();
            if (items == null || items.isEmpty()) {
                throw new IllegalMessageException(Topic.CHECK_QUANTITY.getResponseName(), response.getId(), "items");
            }
            Set<Integer> retainedProductIds = items.stream()
                    .map(Item::getProductId)
                    .collect(Collectors.toSet());
            cartService.clear(memberId, retainedProductIds);
        }
    }
}
