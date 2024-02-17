package com.unboxnow.order.service;

import com.unboxnow.common.exception.NotFoundException;
import com.unboxnow.order.dao.OrderItemRepo;
import com.unboxnow.order.dao.OrderRepo;
import com.unboxnow.order.dto.OrderItemDTO;
import com.unboxnow.order.entity.Order;
import com.unboxnow.order.entity.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepo orderItemRepo;

    private final OrderRepo orderRepo;

    @Autowired
    public OrderItemServiceImpl(OrderItemRepo orderItemRepo, OrderRepo orderRepo) {
        this.orderItemRepo = orderItemRepo;
        this.orderRepo = orderRepo;
    }

    @Override
    public List<OrderItemDTO> findAll() {
        return OrderItemDTO.fromEntities(orderItemRepo.findAll());
    }

    @Override
    public OrderItemDTO findById(int theId) {
        OrderItem item = orderItemRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("OrderItem", theId));
        return OrderItemDTO.fromEntity(item);
    }

    @Override
    public List<OrderItemDTO> findByOrderId(int theId) {
        return OrderItemDTO.fromEntities(orderItemRepo.findByOrderId(theId));
    }

    @Override
    public List<OrderItemDTO> findBySku(String sku) {
        return OrderItemDTO.fromEntities(orderItemRepo.findBySku(sku));
    }

    @Override
    public OrderItemDTO deactivateById(int theId) {
        OrderItem item = orderItemRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("OrderItem", theId));
        item.setActive(false);
        return OrderItemDTO.fromEntity(orderItemRepo.save(item));
    }

    @Override
    public void deleteById(int theId) {
        OrderItem item = orderItemRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("OrderItem", theId));
        Order dbOrder = item.getOrder();
        item.setOrder(null);
        dbOrder.getItems().remove(item);
        dbOrder.update();
        orderRepo.save(dbOrder);
        orderItemRepo.save(item);
        orderItemRepo.deleteById(theId);
    }
}
