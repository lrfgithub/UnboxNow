package com.unboxnow.order.dto;

import com.unboxnow.common.entity.CartItem;
import com.unboxnow.order.entity.OrderItem;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderItemDTO {

    private int id;

    @NotNull
    @Min(1)
    private Integer productId;

    @NotBlank
    private String title;

    @NotBlank
    private String sku;

    @NotNull
    @DecimalMin("0")
    private BigDecimal price;

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotNull
    private Boolean active;

    public OrderItemDTO() {
    }

    public OrderItemDTO(Integer productId,
                        String title,
                        String sku,
                        BigDecimal price,
                        Integer quantity,
                        Boolean active) {
        this.productId = productId;
        this.title = title;
        this.sku = sku;
        this.price = price;
        this.quantity = quantity;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price == null? null : price.setScale(2, RoundingMode.CEILING);
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "OrderItemDTO{" +
                "id=" + id +
                ", productId=" + productId +
                ", title='" + title + '\'' +
                ", sku='" + sku + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", active=" + active +
                '}';
    }

    public static OrderItemDTO fromEntity(OrderItem item) {
        if (item == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(item, OrderItemDTO.class);
    }

    public static List<OrderItemDTO> fromEntities(List<OrderItem> items) {
        if (items == null || items.isEmpty()) return new ArrayList<>();
        return items.stream()
                .map(OrderItemDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public static OrderItemDTO fromCartItem(CartItem cartItem) {
        if (cartItem == null) return null;
        return new OrderItemDTO(
                cartItem.getProductId(),
                cartItem.getTitle(),
                cartItem.getSku(),
                cartItem.getPrice(),
                cartItem.getQuantity(),
                true
        );
    }

    public static List<OrderItemDTO> fromCartItems(List<CartItem> cartItems) {
        if (cartItems == null) return null;
        else if (cartItems.isEmpty()) return new ArrayList<>();
        return cartItems.stream()
                .map(OrderItemDTO::fromCartItem)
                .collect(Collectors.toList());
    }
}
