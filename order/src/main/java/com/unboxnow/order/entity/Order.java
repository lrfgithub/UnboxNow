package com.unboxnow.order.entity;

import com.unboxnow.order.dto.OrderDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "member_id")
    @NotNull
    @Min(1)
    private Integer memberId;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "address_id")
    @NotNull
    @Valid
    private Address address;

    @Column(name = "amount")
    @NotNull
    @DecimalMin("0")
    private BigDecimal amount;

    @Column(name = "total")
    @NotNull
    @Min(0)
    private Integer total;

    @Column(name = "status")
    @NotBlank
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @NotEmpty
    @Valid
    private List<OrderItem> items = new ArrayList<>();

    public Order() {
    }

    public Order(Integer memberId) {
        this.memberId = memberId;
    }

    public Order(int id,
                 Integer memberId,
                 Address address,
                 BigDecimal amount,
                 Integer total,
                 String status,
                 List<OrderItem> items) {
        this.id = id;
        this.memberId = memberId;
        this.address = address;
        this.amount = amount == null? null : amount.setScale(2, RoundingMode.CEILING);
        this.total = total;
        this.status = status;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount == null? null : amount.setScale(2, RoundingMode.CEILING);
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", address=" + address +
                ", amount=" + amount +
                ", total=" + total +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }

    public static Order fromDTO(OrderDTO dto) {
        if (dto == null) return null;
        return new Order(
                dto.getId(),
                dto.getMemberId(),
                Address.fromDTO(dto.getAddressDTO()),
                dto.getAmount(),
                dto.getTotal(),
                dto.getStatus(),
                OrderItem.fromDTOs(dto.getItems())
        );
    }

    public void update() {
        int currTotal = 0;
        BigDecimal currAmount = new BigDecimal(0);
        if (items != null && !items.isEmpty()) {
            for (OrderItem item : items) {
                if (item.getActive() != null && item.getActive()) {
                    currTotal += item.getQuantity();
                    currAmount = currAmount.add(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
                }
            }
        }
        total = currTotal;
        amount = currAmount.setScale(2, RoundingMode.CEILING);
    }
}
