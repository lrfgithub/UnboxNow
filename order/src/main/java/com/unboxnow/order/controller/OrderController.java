package com.unboxnow.order.controller;

import com.unboxnow.order.dto.MemberAddressDTO;
import com.unboxnow.order.dto.OrderDTO;
import com.unboxnow.order.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderDTO> findAll() {
        return orderService.findAll();
    }

    @GetMapping("/{theId}")
    public OrderDTO findById(@Min(1) @PathVariable int theId) {
        return orderService.findById(theId);
    }

    @GetMapping("/address/{theId}")
    public OrderDTO findByAddressId(@Min(1) @PathVariable int theId) {
        return orderService.findByAddressId(theId);
    }

    @GetMapping("/member/{theId}")
    public List<OrderDTO> findByMemberId(@Min(1) @PathVariable int theId) {
        return orderService.findByMemberId(theId);
    }

    @PostMapping
    public OrderDTO submitAddress(@Valid @RequestBody MemberAddressDTO dto) {
        return orderService.submitAddress(dto);
    }

    @PostMapping("/{theId}")
    public void submitPayment(@Min(1) @PathVariable int theId) {
        orderService.submitPayment(theId);
    }

    @PutMapping("/{theId}")
    public OrderDTO cancelById(@Min(1) @PathVariable int theId) {
        return orderService.cancelById(theId);
    }
}
