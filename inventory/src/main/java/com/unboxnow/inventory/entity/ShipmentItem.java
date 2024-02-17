package com.unboxnow.inventory.entity;

import com.unboxnow.inventory.dto.ShipmentItemDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "shipment_item")
public class ShipmentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "shipment_id")
    @Valid
    private Shipment shipment;

    @Column(name = "title")
    @NotBlank
    private String title;

    @Column(name = "SKU")
    @NotBlank
    private String sku;

    @Column(name = "quantity")
    @NotNull
    @Min(1)
    private Integer quantity;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public ShipmentItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ShipmentItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", shipment=" + shipment +
                ", sku='" + sku + '\'' +
                ", quantity=" + quantity +
                ", createdAt=" + createdAt +
                '}';
    }

    public static ShipmentItem fromDTO(ShipmentItemDTO dto) {
        if (dto == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        PropertyMap<ShipmentItemDTO, ShipmentItem> shipmentItemDTOMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().getShipment().setId(source.getShipmentId());
                map().getShipment().setOrderId(source.getShipmentOrderId());
                map().getShipment().setTotal(source.getShipmentTotal());
            }
        };
        modelMapper.addMappings(shipmentItemDTOMap);
        return modelMapper.map(dto, ShipmentItem.class);
    }

    public static List<ShipmentItem> fromDTOs(List<ShipmentItemDTO> dtos) {
        if (dtos == null) return null;
        else if (dtos.isEmpty()) return new ArrayList<>();
        return dtos.stream()
                .map(ShipmentItem::fromDTO)
                .collect(Collectors.toList());
    }
}
