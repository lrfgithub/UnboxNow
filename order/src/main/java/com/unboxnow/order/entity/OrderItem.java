package com.unboxnow.order.entity;

import com.unboxnow.order.dto.OrderItemDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
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

@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "order_id")
    @Valid
    private Order order;

    @Column(name = "product_id")
    @NotNull
    @Min(1)
    private Integer productId;

    @Column(name = "title")
    @NotBlank
    private String title;

    @Column(name = "SKU")
    @NotBlank
    private String sku;

    @Column(name = "price")
    @NotNull
    @DecimalMin("0")
    private BigDecimal price;

    @Column(name = "quantity")
    @NotNull
    @Min(1)
    private Integer quantity;

    @Column(name = "active")
    @NotNull
    private Boolean active;

    public OrderItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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
        return "OrderItem{" +
                "id=" + id +
                ", order=" + order +
                ", productId=" + productId +
                ", title='" + title + '\'' +
                ", sku='" + sku + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", active=" + active +
                '}';
    }

    public static OrderItem fromDTO(OrderItemDTO dto) {
        if (dto == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, OrderItem.class);
    }

    public static List<OrderItem> fromDTOs(List<OrderItemDTO> dtos) {
        if (dtos == null) return null;
        else if (dtos.isEmpty()) return new ArrayList<>();
        return dtos.stream()
                .map(OrderItem::fromDTO)
                .collect(Collectors.toList());
    }

}
