package com.unboxnow.inventory.dto;

import com.unboxnow.common.entity.InventoryItem;
import com.unboxnow.inventory.entity.ShipmentItem;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShipmentItemDTO {

    private int id;

    private int shipmentId;

    private Integer shipmentOrderId;

    private Integer shipmentTotal;

    @NotBlank
    private String title;

    @NotBlank
    private String sku;

    @NotNull
    @Min(1)
    private Integer quantity;

    public ShipmentItemDTO() {
    }

    public ShipmentItemDTO(String title, String sku, Integer quantity) {
        this.title = title;
        this.sku = sku;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }

    public Integer getShipmentOrderId() {
        return shipmentOrderId;
    }

    public void setShipmentOrderId(Integer shipmentOrderId) {
        this.shipmentOrderId = shipmentOrderId;
    }

    public Integer getShipmentTotal() {
        return shipmentTotal;
    }

    public void setShipmentTotal(Integer shipmentTotal) {
        this.shipmentTotal = shipmentTotal;
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

    @Override
    public String toString() {
        return "ShipmentItemDTO{" +
                "id=" + id +
                ", shipmentId=" + shipmentId +
                ", shipmentOrderId=" + shipmentOrderId +
                ", shipmentTotal=" + shipmentTotal +
                ", title='" + title + '\'' +
                ", sku='" + sku + '\'' +
                ", quantity=" + quantity +
                '}';
    }

    public static ShipmentItemDTO fromEntity(ShipmentItem item) {
        if (item == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        PropertyMap<ShipmentItem, ShipmentItemDTO> shipmentItemMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setShipmentId(source.getShipment().getId());
                map().setShipmentOrderId(source.getShipment().getOrderId());
                map().setShipmentTotal(source.getShipment().getTotal());
            }
        };
        modelMapper.addMappings(shipmentItemMap);
        return modelMapper.map(item, ShipmentItemDTO.class);
    }

    public static ShipmentItemDTO fromEntity(InventoryItem item) {
        if (item == null) return null;
        return new ShipmentItemDTO(
                item.getTitle(),
                item.getSku(),
                item.getQuantity()
        );
    }

    public static List<ShipmentItemDTO> fromEntities(List<ShipmentItem> items) {
        if (items == null) return null;
        else if (items.isEmpty()) return new ArrayList<>();
        return items.stream()
                .map(ShipmentItemDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
