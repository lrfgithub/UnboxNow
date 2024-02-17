package com.unboxnow.cart.service;

import com.unboxnow.cart.dto.CartDTO;
import com.unboxnow.cart.dto.CartItemDTO;
import com.unboxnow.common.entity.CartItem;

import java.util.List;
import java.util.Set;

public interface CartService {

    CartDTO findByMemberId(int memberId);

    CartDTO add(CartItemDTO dto);

    CartDTO subtract(CartItemDTO dto);

    void deleteItem(CartItemDTO dto);

    void deleteItemsByMemberId(int memberId);

    List<CartItem> placeOrder(int memberId);

    CartDTO clear(int memberId, Set<Integer> retainedProductIds);
}
