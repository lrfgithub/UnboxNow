package com.unboxnow.inventory.entity;

import com.unboxnow.inventory.dto.InventoryDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "warehouse_id")
    @Valid
    private Warehouse warehouse;

    @Column(name = "title")
    @NotBlank
    private String title;

    @Column(name = "SKU")
    @NotBlank
    private String sku;

    @Column(name = "quantity")
    @NotNull
    @Min(0)
    private Integer quantity;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    public Inventory() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
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

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", warehouse=" + warehouse +
                ", title='" + title + '\'' +
                ", sku='" + sku + '\'' +
                ", quantity=" + quantity +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }

    public static Inventory fromDTO(InventoryDTO dto) {
        if (dto == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        PropertyMap<InventoryDTO, Inventory> inventoryDTOMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().getWarehouse().setId(source.getWarehouseId());
                map().getWarehouse().setTitle(source.getWarehouseTitle());
            }
        };
        modelMapper.addMappings(inventoryDTOMap);
        return modelMapper.map(dto, Inventory.class);
    }
}
