package com.unboxnow.inventory.service;

import com.unboxnow.inventory.dto.ShipmentItemDTO;

import java.util.List;

public interface ShipmentItemService {

    List<ShipmentItemDTO> findAll();

    ShipmentItemDTO findById(int theId);

    List<ShipmentItemDTO> findByShipmentId(int theId);

    List<ShipmentItemDTO> findBySku(String sku);

}
