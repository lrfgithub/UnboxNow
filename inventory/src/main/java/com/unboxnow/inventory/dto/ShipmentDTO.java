package com.unboxnow.inventory.dto;

import com.unboxnow.inventory.entity.Shipment;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShipmentDTO {

    private int id;

    @NotNull
    @Valid
    private AddressDTO departureAddressDTO;

    @NotNull
    @Valid
    private AddressDTO destinationAddressDTO;

    @NotNull
    @Min(1)
    private Integer orderId;

    private Integer total;

    private String status;

    private String trackingNumber;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    @NotEmpty
    @Valid
    private List<ShipmentItemDTO> shipmentItems = new ArrayList<>();

    public ShipmentDTO() {
    }

    public ShipmentDTO(AddressDTO departureAddressDTO,
                       AddressDTO destinationAddressDTO,
                       Integer orderId) {
        this.departureAddressDTO = departureAddressDTO;
        this.destinationAddressDTO = destinationAddressDTO;
        this.orderId = orderId;
    }

    public ShipmentDTO(int id,
                       AddressDTO departureAddressDTO,
                       AddressDTO destinationAddressDTO,
                       Integer orderId,
                       Integer total,
                       String status,
                       String trackingNumber,
                       LocalDateTime createdAt,
                       LocalDateTime modifiedAt,
                       List<ShipmentItemDTO> shipmentItems) {
        this.id = id;
        this.departureAddressDTO = departureAddressDTO;
        this.destinationAddressDTO = destinationAddressDTO;
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

    public AddressDTO getDepartureAddressDTO() {
        return departureAddressDTO;
    }

    public void setDepartureAddressDTO(AddressDTO departureAddressDTO) {
        this.departureAddressDTO = departureAddressDTO;
    }

    public AddressDTO getDestinationAddressDTO() {
        return destinationAddressDTO;
    }

    public void setDestinationAddressDTO(AddressDTO destinationAddressDTO) {
        this.destinationAddressDTO = destinationAddressDTO;
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

    public List<ShipmentItemDTO> getShipmentItems() {
        return shipmentItems;
    }

    public void setShipmentItems(List<ShipmentItemDTO> shipmentItems) {
        this.shipmentItems = shipmentItems;
    }

    @Override
    public String toString() {
        return "ShipmentDTO{" +
                "id=" + id +
                ", departureAddressDTO=" + departureAddressDTO +
                ", destinationAddressDTO=" + destinationAddressDTO +
                ", orderId=" + orderId +
                ", total=" + total +
                ", status='" + status + '\'' +
                ", trackingNumber='" + trackingNumber + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", shipmentItems=" + shipmentItems +
                '}';
    }

    public static ShipmentDTO fromEntity(Shipment shipment) {
        if (shipment == null) return null;
        return new ShipmentDTO(
                shipment.getId(),
                AddressDTO.fromEntity(shipment.getDepartureAddress()),
                AddressDTO.fromEntity(shipment.getDestinationAddress()),
                shipment.getOrderId(),
                shipment.getTotal(),
                shipment.getStatus(),
                shipment.getTrackingNumber(),
                shipment.getCreatedAt(),
                shipment.getModifiedAt(),
                ShipmentItemDTO.fromEntities(shipment.getShipmentItems())
        );
    }

    public void add(ShipmentItemDTO item) {
        if (item == null) return;
        if (this.shipmentItems == null) this.shipmentItems = new ArrayList<>();
        this.shipmentItems.add(item);
    }
}
