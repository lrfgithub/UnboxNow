package com.unboxnow.order.dao;

import com.unboxnow.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepo extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> findByOrderId(int theId);

    List<OrderItem> findBySku(String sku);

}
