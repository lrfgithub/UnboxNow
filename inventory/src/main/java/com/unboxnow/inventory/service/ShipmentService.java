package com.unboxnow.inventory.service;

import com.unboxnow.inventory.dto.ShipmentDTO;

import java.util.List;

public interface ShipmentService {

    List<ShipmentDTO> findAll();

    ShipmentDTO findById(int theId);

    List<ShipmentDTO> findByDepartureAddressId(int theId);

    ShipmentDTO findByDestinationAddressId(int theId);

    ShipmentDTO create(ShipmentDTO dto);

    void deleteById(int theId);
}
