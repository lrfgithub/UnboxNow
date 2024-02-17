package com.unboxnow.order.service;

import com.unboxnow.order.dto.OrderItemDTO;

import java.util.List;

public interface OrderItemService {

    List<OrderItemDTO> findAll();

    OrderItemDTO findById(int theId);

    List<OrderItemDTO> findByOrderId(int theId);

    List<OrderItemDTO> findBySku(String sku);

    OrderItemDTO deactivateById(int theId);

    void deleteById(int theId);

}
