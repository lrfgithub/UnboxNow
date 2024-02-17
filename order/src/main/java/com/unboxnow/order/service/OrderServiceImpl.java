package com.unboxnow.order.service;

import com.unboxnow.common.constant.Topic;
import com.unboxnow.common.entity.UniversalAddress;
import com.unboxnow.common.exception.CustomInterruptedException;
import com.unboxnow.common.exception.IllegalValueException;
import com.unboxnow.common.exception.NotFoundException;
import com.unboxnow.common.message.CarrierMessage;
import com.unboxnow.order.constant.EntityNames;
import com.unboxnow.order.constant.OrderState;
import com.unboxnow.order.dao.OrderItemRepo;
import com.unboxnow.order.dao.OrderRepo;
import com.unboxnow.order.dto.MemberAddressDTO;
import com.unboxnow.order.dto.OrderDTO;
import com.unboxnow.order.entity.Order;
import com.unboxnow.order.entity.OrderItem;
import com.unboxnow.order.messaging.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepo orderRepo;

    private final OrderItemRepo orderItemRepo;

    private final HashOperations<String, Integer, Integer> orderOps;

    private final Producer producer;

    @Autowired
    public OrderServiceImpl(OrderRepo orderRepo,
                            OrderItemRepo orderItemRepo,
                            RedisTemplate<String, String> orderRedisTemplate,
                            Producer producer) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.orderOps = orderRedisTemplate.opsForHash();
        this.producer = producer;
    }

    @Override
    public List<OrderDTO> findAll() {
        return OrderDTO.fromEntities(orderRepo.findAll());
    }

    @Override
    public OrderDTO findById(int theId) {
        Order order = orderRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Order", theId));
        return OrderDTO.fromEntity(order);
    }

    @Override
    public OrderDTO findByAddressId(int theId) {
        Order order = orderRepo.findByAddressId(theId)
                .orElseThrow(() -> new NotFoundException("Order", "addressId", theId));
        return OrderDTO.fromEntity(order);
    }

    @Override
    public List<OrderDTO> findByMemberId(int theId) {
        return OrderDTO.fromEntities(orderRepo.findByMemberId(theId));
    }

    @Override
    public OrderDTO submitAddress(MemberAddressDTO dto) {
        int memberId = dto.getMemberId();
        CarrierMessage<Integer> request = new CarrierMessage<>(memberId, dto.getAddressId());
        producer.publish(request, Topic.FETCH_ADDRESS);
        int orderId = getOrderId(memberId);
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order", orderId));
        return OrderDTO.fromEntity(order);
    }

    @Override
    public void submitPayment(int theId) {
        Order order = orderRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Order", theId));
        OrderState state = OrderState.fromValue(order.getStatus());
        if (state == OrderState.CANCELLING) return;
        UniversalAddress address = new UniversalAddress(
                order.getAddress().getName(),
                order.getAddress().getMobile(),
                order.getAddress().getEmail(),
                order.getAddress().getAddress1(),
                order.getAddress().getAddress2(),
                order.getAddress().getCity(),
                order.getAddress().getState(),
                order.getAddress().getZip()
        );
        CarrierMessage<UniversalAddress> request = new CarrierMessage<>(order.getId(), address);
        producer.publish(request, Topic.START_SHIPMENT);
    }

    @Override
    public OrderDTO create(OrderDTO dto) {
        Order order = Order.fromDTO(dto);
        order.setId(0);
        order.setStatus(OrderState.CONFIRMING.getValue());
        order.getAddress().setId(0);
        for (OrderItem item : order.getItems()) {
            item.setOrder(order);
            item.setId(0);
        }
        order.update();
        return OrderDTO.fromEntity(orderRepo.save(order));
    }

    @Override
    public void updateStatus(int theId) {
        Order order = orderRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Order", theId));
        order.update();
        OrderState state = OrderState.fromValue(order.getStatus());
        state = order.getTotal() > 0? state.nextState() : state.abort();
        order.setStatus(state.getValue());
        orderRepo.save(order);
    }

    @Override
    public OrderDTO cancelById(int theId) {
        Order order = orderRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Order", theId));
        order.getItems().forEach(item -> {
            item.setActive(false);
            orderItemRepo.save(item);
        });
        order.setTotal(0);
        order.setAmount(new BigDecimal(0));
        order.setStatus(OrderState.CANCELLING.getValue());
        return OrderDTO.fromEntity(orderRepo.save(order));
    }

    @Override
    public void deleteById(int theId) {
        Optional<Order> opt = orderRepo.findById(theId);
        if (opt.isEmpty()) {
            throw new NotFoundException("Order", theId);
        }
        orderRepo.deleteById(theId);
    }

    private Integer getOrderId(int memberId) {
        int attempts = 20, interval = 50;
        Integer orderId = null;
        try {
            for (int i = 0; i < attempts; i++) {
                if (orderOps.hasKey(EntityNames.ORDER, memberId)) {
                    orderId = orderOps.get(EntityNames.ORDER, memberId);
                    orderOps.delete(EntityNames.ORDER, memberId);
                    break;
                }
                Thread.sleep(interval);
            }
        } catch (InterruptedException ex) {
            throw new CustomInterruptedException(EntityNames.ORDER, memberId);
        }
        if (orderId == null) {
            throw new IllegalValueException(EntityNames.ORDER, memberId);
        }
        return orderId;
    }

}
