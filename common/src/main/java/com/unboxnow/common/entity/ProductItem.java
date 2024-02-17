package com.unboxnow.common.entity;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProductItem extends Item {

    @NotNull
    @DecimalMin("0")
    private BigDecimal price;

    public ProductItem() {
    }

    public ProductItem(Integer productId, String title, String sku, BigDecimal price) {
        super(productId, title, sku);
        this.price = price == null? null : price.setScale(2, RoundingMode.CEILING);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price == null? null : price.setScale(2, RoundingMode.CEILING);
    }

    @Override
    public String toString() {
        return "ProductItem{" +
                "productId=" + getProductId() +
                ", title='" + getTitle() + '\'' +
                ", sku='" + getSku() + '\'' +
                ", price=" + price +
                '}';
    }
}
