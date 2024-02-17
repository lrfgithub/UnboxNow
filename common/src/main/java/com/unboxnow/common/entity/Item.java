package com.unboxnow.common.entity;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Item {

    @NotNull
    @Min(1)
    private Integer productId;

    @NotBlank
    private String title;

    @NotBlank
    private String sku;

    public Item() {
    }

    public Item(Integer productId, String title, String sku) {
        this.productId = productId;
        this.title = title;
        this.sku = sku;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @Override
    public String toString() {
        return "Item{" +
                "productId=" + productId +
                ", title='" + title + '\'' +
                ", sku='" + sku + '\'' +
                '}';
    }
}
