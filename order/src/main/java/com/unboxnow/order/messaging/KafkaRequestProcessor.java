package com.unboxnow.order.messaging;

import com.unboxnow.common.constant.Topic;
import com.unboxnow.common.entity.CartItem;
import com.unboxnow.common.entity.QueryItem;
import com.unboxnow.common.entity.UniversalAddress;
import com.unboxnow.common.exception.IllegalValueException;
import com.unboxnow.common.message.ConfirmationMessage;
import com.unboxnow.common.message.ContainerMessage;
import com.unboxnow.common.message.Message;
import com.unboxnow.common.message.RetrieverMessage;
import com.unboxnow.order.constant.EntityNames;
import com.unboxnow.order.dto.AddressDTO;
import com.unboxnow.order.dto.OrderDTO;
import com.unboxnow.order.dto.OrderItemDTO;
import com.unboxnow.order.service.OrderItemService;
import com.unboxnow.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaRequestProcessor {

    private final OrderService orderService;

    private final OrderItemService orderItemService;

    private final HashOperations<String, Integer, ContainerMessage<CartItem>> preorderOps;

    private final HashOperations<String, Integer, Integer> orderOps;

    private final Producer producer;

    @Autowired
    public KafkaRequestProcessor(OrderService orderService,
                                 OrderItemService orderItemService,
                                 RedisTemplate<String, String> preorderRedisTemplate,
                                 RedisTemplate<String, String> orderRedisTemplate,
                                 Producer producer) {
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.preorderOps = preorderRedisTemplate.opsForHash();
        this.orderOps = orderRedisTemplate.opsForHash();
        this.producer = producer;
    }

    public void receivePreorder(ContainerMessage<CartItem> request) {
        preorderOps.put(EntityNames.PREORDER, request.getEntityId(), request);
    }

    public void receiveAddress(RetrieverMessage<UniversalAddress> userResponse) {
        int memberId = userResponse.getEntityId();
        ContainerMessage<CartItem> cartRequest = preorderOps.get(EntityNames.PREORDER, memberId);
        if (cartRequest == null) {
            throw new IllegalValueException(EntityNames.PREORDER, memberId);
        }
        if (userResponse.getValid() == null || !userResponse.getValid()) {
            ConfirmationMessage<CartItem> orderResponse = new ConfirmationMessage<>(memberId, false);
            orderResponse.setItems(cartRequest.getItems());
            producer.publish(orderResponse, Topic.CHECK_OUT, cartRequest.getId());
            return;
        }
        OrderDTO orderDTO = new OrderDTO(
                memberId,
                AddressDTO.fromEntity(userResponse.getData()),
                OrderItemDTO.fromCartItems(cartRequest.getItems()));
        orderDTO = orderService.create(orderDTO);
        int orderId = orderDTO.getId();
        orderOps.put(EntityNames.ORDER, memberId, orderId);
        ContainerMessage<QueryItem> lockRequest = new ContainerMessage<>(
                orderId,
                QueryItem.fromCartItems(cartRequest.getItems())
        );
        producer.publish(lockRequest, Topic.LOCK_QUANTITY);
    }

    public void receiveLock(ConfirmationMessage<QueryItem> inventoryResponse) {
        int orderId = inventoryResponse.getEntityId();
        OrderDTO orderDTO = orderService.findById(orderId);
        int memberId = orderDTO.getMemberId();
        ContainerMessage<CartItem> cartRequest = preorderOps.get(EntityNames.PREORDER, memberId);
        if (cartRequest == null) {
            throw new IllegalValueException(EntityNames.PREORDER, memberId);
        }
        preorderOps.delete(EntityNames.PREORDER, memberId);
        ConfirmationMessage<CartItem> orderResponse = new ConfirmationMessage<>(memberId);
        if (inventoryResponse.getValid()) {
            orderService.updateStatus(orderId);
            orderResponse.setValid(true);
        } else {
            Map<Integer, CartItem> cartMap = new HashMap<>();
            for (CartItem cartItem : cartRequest.getItems()) {
                cartMap.put(cartItem.getProductId(), cartItem);
            }
            for (OrderItemDTO orderItemDTO : orderDTO.getItems()) {
                int productId = orderItemDTO.getProductId();
                if (cartMap.containsKey(productId)) {
                    orderItemService.deactivateById(productId);
                }
            }
            orderService.updateStatus(orderId);
            orderResponse.setValid(false);
            for (QueryItem queryItem : inventoryResponse.getItems()) {
                int productId = queryItem.getProductId();
                if (cartMap.containsKey(productId)) {
                    orderResponse.add(cartMap.get(productId));
                }
            }
        }
        producer.publish(orderResponse, Topic.CHECK_OUT, cartRequest.getId());
    }

    public void unlock(Message update) {
        orderService.cancelById(update.getEntityId());
    }

    public void startShipment(RetrieverMessage<Boolean> response) {
        if (response.getData() != null && response.getData()) {
            orderService.updateStatus(response.getEntityId());
        }
    }

}
