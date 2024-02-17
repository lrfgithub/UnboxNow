package com.unboxnow.order.controller;

import com.unboxnow.order.dto.OrderItemDTO;
import com.unboxnow.order.service.OrderItemService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-items")
@Validated
public class OrderItemController {

    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @GetMapping
    public List<OrderItemDTO> findAll() {
        return orderItemService.findAll();
    }

    @GetMapping("/{theId}")
    public OrderItemDTO findById(@Min(1) @PathVariable int theId) {
        return orderItemService.findById(theId);
    }

    @GetMapping("/order/{theId}")
    public List<OrderItemDTO> findByOrderId(@Min(1) @PathVariable int theId) {
        return orderItemService.findByOrderId(theId);
    }

    @GetMapping("/sku/{sku}")
    public List<OrderItemDTO> findBySku(@NotBlank @PathVariable String sku) {
        return orderItemService.findBySku(sku);
    }

    @PutMapping("/{theId}")
    public OrderItemDTO deactivateById(@Min(1) @PathVariable int theId) {
        return orderItemService.deactivateById(theId);
    }
}
