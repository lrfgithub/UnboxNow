package com.unboxnow.common.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QueryItem extends Item {

    @NotNull
    @Min(1)
    private Integer quantity;

    public QueryItem() {
    }

    public QueryItem(Integer productId, String title, String sku, Integer quantity) {
        super(productId, title, sku);
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
        return "QueryItem{" +
                "productId=" + getProductId() +
                ", title='" + getTitle() + '\'' +
                ", sku='" + getSku() + '\'' +
                ", quantity=" + quantity +
                '}';
    }

    public static QueryItem fromCartItem(CartItem cartItem) {
        if (cartItem == null) return null;
        return new QueryItem(
                cartItem.getProductId(),
                cartItem.getTitle(),
                cartItem.getSku(),
                cartItem.getQuantity()
        );
    }

    public static List<QueryItem> fromCartItems(List<CartItem> cartItems) {
        if (cartItems == null) return null;
        else if (cartItems.isEmpty()) return new ArrayList<>();
        return cartItems.stream()
                .map(QueryItem::fromCartItem)
                .collect(Collectors.toList());
    }

    public static QueryItem fromProductItem(ProductItem productItem, int quantity) {
        if (productItem == null) return null;
        return new QueryItem(
                productItem.getProductId(),
                productItem.getTitle(),
                productItem.getSku(),
                quantity
        );
    }
}
