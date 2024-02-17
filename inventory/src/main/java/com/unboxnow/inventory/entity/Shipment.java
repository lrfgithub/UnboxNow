package com.unboxnow.inventory.entity;

import com.unboxnow.inventory.dto.ShipmentDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shipment")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "departure_address_id")
    @NotNull
    @Valid
    private Address departureAddress;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "destination_address_id")
    @NotNull
    @Valid
    private Address destinationAddress;

    @Column(name = "order_id")
    @NotNull
    @Min(1)
    private Integer orderId;

    @Column(name = "total")
    @NotNull
    @Min(1)
    private Integer total;

    @Column(name = "status")
    private String status;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "shipment", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @NotEmpty
    @Valid
    private List<ShipmentItem> shipmentItems = new ArrayList<>();

    public Shipment() {
    }

    public Shipment(int id,
                    Address departureAddress,
                    Address destinationAddress,
                    Integer orderId,
                    Integer total,
                    String status,
                    String trackingNumber,
                    LocalDateTime createdAt,
                    LocalDateTime modifiedAt,
                    List<ShipmentItem> shipmentItems) {
        this.id = id;
        this.departureAddress = departureAddress;
        this.destinationAddress = destinationAddress;
        this.orderId = orderId;
        this.total = total;
        this.status = status;
        this.trackingNumber = trackingNumber;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.shipmentItems = shipmentItems;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Address getDepartureAddress() {
        return departureAddress;
    }

    public void setDepartureAddress(Address departureAddress) {
        this.departureAddress = departureAddress;
    }

    public Address getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(Address destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
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

    public List<ShipmentItem> getShipmentItems() {
        return shipmentItems;
    }

    public void setShipmentItems(List<ShipmentItem> shipmentItems) {
        this.shipmentItems = shipmentItems;
    }

    @Override
    public String toString() {
        return "Shipment{" +
                "id=" + id +
                ", departureAddress=" + departureAddress +
                ", destinationAddress=" + destinationAddress +
                ", orderId=" + orderId +
                ", total=" + total +
                ", status='" + status + '\'' +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", shipmentItems=" + shipmentItems +
                '}';
    }

    public static Shipment fromDTO(ShipmentDTO dto) {
        if (dto == null) return null;
        return new Shipment(
                dto.getId(),
                Address.fromDTO(dto.getDepartureAddressDTO()),
                Address.fromDTO(dto.getDestinationAddressDTO()),
                dto.getOrderId(),
                dto.getTotal(),
                dto.getStatus(),
                dto.getTrackingNumber(),
                dto.getCreatedAt(),
                dto.getModifiedAt(),
                ShipmentItem.fromDTOs(dto.getShipmentItems())
        );
    }

    public void update() {
        if (shipmentItems == null || shipmentItems.isEmpty()) return;
        this.total = shipmentItems.stream()
                .map(ShipmentItem::getQuantity)
                .mapToInt(Integer::intValue)
                .sum();
    }
}
