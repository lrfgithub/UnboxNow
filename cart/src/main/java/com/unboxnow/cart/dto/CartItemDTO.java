package com.unboxnow.cart.dto;

import jakarta.validation.constraints.Min;

public class CartItemDTO {

    @Min(1)
    private int memberId;

    @Min(1)
    private int productId;

    @Min(1)
    private int quantity;

    public CartItemDTO() {
    }

    public CartItemDTO(int memberId, int productId, int quantity) {
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItemDTO{" +
                "memberId=" + memberId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
