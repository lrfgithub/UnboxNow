package com.unboxnow.cart.controller;

import com.unboxnow.cart.dto.CartDTO;
import com.unboxnow.cart.dto.CartItemDTO;
import com.unboxnow.cart.service.CartService;
import com.unboxnow.common.entity.CartItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
@Validated
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{memberId}")
    public CartDTO findByMemberId(@Min(1) @PathVariable int memberId){
        return cartService.findByMemberId(memberId);
    }

    @PostMapping("/add")
    public void add(@Valid @RequestBody CartItemDTO dto) {
        cartService.add(dto);
    }

    @PostMapping("/sub")
    public void subtract(@Valid @RequestBody CartItemDTO dto) {
        cartService.subtract(dto);
    }

    @PostMapping("/{memberId}")
    public List<CartItem> placeOrder(@Min(1) @PathVariable int memberId) {
        return cartService.placeOrder(memberId);
    }

    @DeleteMapping
    public void deleteItem(@Valid @RequestBody CartItemDTO dto) {
        cartService.deleteItem(dto);
    }

    @DeleteMapping("/{memberId}")
    public void deleteItemsByMemberId(@Min(1) @PathVariable int memberId) {
        cartService.deleteItemsByMemberId(memberId);
    }
}
