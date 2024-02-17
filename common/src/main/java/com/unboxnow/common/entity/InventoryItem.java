package com.unboxnow.common.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class InventoryItem extends QueryItem {

    @NotNull
    @Min(1)
    private Integer inventoryId;

    @NotNull
    @Min(1)
    private Integer addressId;

    public InventoryItem() {
    }

    public InventoryItem(Integer productId,
                         String title,
                         String sku,
                         Integer quantity,
                         Integer inventoryId,
                         Integer addressId) {
        super(productId, title, sku, quantity);
        this.inventoryId = inventoryId;
        this.addressId = addressId;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    @Override
    public String toString() {
        return "InventoryItem{" +
                "productId=" + getProductId() +
                ", title='" + getTitle() + '\'' +
                ", sku='" + getSku() + '\'' +
                ", quantity=" + getQuantity() +
                ", inventoryId=" + inventoryId +
                ", addressId=" + addressId +
                '}';
    }

    public static InventoryItem fromOrderItem(
            QueryItem queryItem, Integer quantity, Integer inventoryId, Integer addressId) {
        if (queryItem == null) return null;
        return new InventoryItem(
                queryItem.getProductId(),
                queryItem.getTitle(),
                queryItem.getSku(),
                quantity,
                inventoryId,
                addressId
        );
    }
}
