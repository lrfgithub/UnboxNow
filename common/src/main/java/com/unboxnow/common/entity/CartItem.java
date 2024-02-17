package com.unboxnow.common.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CartItem extends ProductItem {

    @NotNull
    @Min(1)
    private Integer quantity;

    public CartItem() {
    }

    public CartItem(Integer productId, String title, String sku, BigDecimal price, Integer quantity) {
        super(productId, title, sku, price);
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "productId=" + getProductId() +
                ", title='" + getTitle() + '\'' +
                ", sku='" + getSku() + '\'' +
                ", price=" + getPrice() +
                ", quantity=" + quantity +
                '}';
    }

    public static CartItem fromProductItem(ProductItem productItem, Integer quantity) {
        if (productItem == null) return null;
        return new CartItem(
                productItem.getProductId(),
                productItem.getTitle(),
                productItem.getSku(),
                productItem.getPrice(),
                quantity
        );
    }
}
