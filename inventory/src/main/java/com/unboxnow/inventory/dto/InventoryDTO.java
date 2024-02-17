package com.unboxnow.inventory.dto;

import com.unboxnow.inventory.entity.Inventory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryDTO {

    private int id;

    @Min(1)
    private int warehouseId;

    private String warehouseTitle;

    @NotBlank
    private String title;

    @NotBlank
    private String sku;

    @NotNull
    @Min(0)
    private Integer quantity;

    public InventoryDTO() {
    }

    public InventoryDTO(int warehouseId, String title, String sku, Integer quantity) {
        this.warehouseId = warehouseId;
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

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseTitle() {
        return warehouseTitle;
    }

    public void setWarehouseTitle(String warehouseTitle) {
        this.warehouseTitle = warehouseTitle;
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
        return "InventoryDTO{" +
                "id=" + id +
                ", warehouseId=" + warehouseId +
                ", warehouseTitle='" + warehouseTitle + '\'' +
                ", title='" + title + '\'' +
                ", sku='" + sku + '\'' +
                ", quantity=" + quantity +
                '}';
    }

    public static InventoryDTO fromEntity(Inventory inventory) {
        if (inventory == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        PropertyMap<Inventory, InventoryDTO> inventoryMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setWarehouseId(source.getWarehouse().getId());
                map().setWarehouseTitle(source.getWarehouse().getTitle());
            }
        };
        modelMapper.addMappings(inventoryMap);
        return modelMapper.map(inventory, InventoryDTO.class);
    }

    public static List<InventoryDTO> fromEntities(List<Inventory> inventories) {
        if (inventories == null) return null;
        else if (inventories.isEmpty()) return new ArrayList<>();
        return inventories.stream()
                .map(InventoryDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
