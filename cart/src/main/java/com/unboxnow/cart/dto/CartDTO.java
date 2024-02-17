package com.unboxnow.cart.dto;

import com.unboxnow.common.entity.CartItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class CartDTO {

    private Integer memberId;

    private Integer total;

    private BigDecimal amount;

    private List<CartItem> items;

    public CartDTO() {
    }

    public CartDTO(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.CEILING);
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "CartDTO{" +
                "memberId=" + memberId +
                ", total=" + total +
                ", amount=" + amount +
                ", items=" + items +
                '}';
    }

    public void add(CartItem item) {
        if (item == null) return;
        if (items == null) items = new ArrayList<>();
        items.add(item);
    }

    public void update() {
        total = 0;
        amount = new BigDecimal("0");
        if (items != null && !items.isEmpty()) {
            for (CartItem item : items) {
                total += item.getQuantity();
                amount = amount.add(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
            }
        }
        amount = amount.setScale(2, RoundingMode.CEILING);
    }
}
