package com.unboxnow.order.dto;

import com.unboxnow.order.entity.Order;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDTO {

    private int id;

    @NotNull
    @Min(1)
    private Integer memberId;

    @NotNull
    @Valid
    private AddressDTO addressDTO;

    private BigDecimal amount;

    private Integer total;

    @NotBlank
    private String status;

    @NotEmpty
    @Valid
    private List<OrderItemDTO> items = new ArrayList<>();

    public OrderDTO() {
    }

    public OrderDTO(Integer memberId) {
        this.memberId = memberId;
    }

    public OrderDTO(Integer memberId, AddressDTO addressDTO) {
        this.memberId = memberId;
        this.addressDTO = addressDTO;
    }

    public OrderDTO(Integer memberId, AddressDTO addressDTO, List<OrderItemDTO> items) {
        this.memberId = memberId;
        this.addressDTO = addressDTO;
        this.items = items;
    }

    public OrderDTO(int id,
                    Integer memberId,
                    AddressDTO addressDTO,
                    BigDecimal amount,
                    Integer total,
                    String status,
                    List<OrderItemDTO> items) {
        this.id = id;
        this.memberId = memberId;
        this.addressDTO = addressDTO;
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

    public AddressDTO getAddressDTO() {
        return addressDTO;
    }

    public void setAddressDTO(AddressDTO addressDTO) {
        this.addressDTO = addressDTO;
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

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", addressDTO=" + addressDTO +
                ", amount=" + amount +
                ", total=" + total +
                ", status='" + status + '\'' +
                ", items=" + items +
                '}';
    }

    public static OrderDTO fromEntity(Order order) {
        if (order == null) return null;
        return new OrderDTO(
                order.getId(),
                order.getMemberId(),
                AddressDTO.fromEntity(order.getAddress()),
                order.getAmount(),
                order.getTotal(),
                order.getStatus(),
                OrderItemDTO.fromEntities(order.getItems())
        );
    }

    public static List<OrderDTO> fromEntities(List<Order> orders) {
        if (orders == null) return null;
        else if (orders.isEmpty()) return new ArrayList<>();
        return orders.stream()
                .map(OrderDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public void add(OrderItemDTO dto) {
        if (dto == null) return;
        if (items == null) items = new ArrayList<>();
        items.add(dto);
    }
}
