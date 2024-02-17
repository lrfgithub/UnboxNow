package com.unboxnow.inventory.service;

import com.unboxnow.inventory.dto.AddressDTO;

import java.util.List;
import java.util.Map;

public interface AddressService {

    List<AddressDTO> findAll();

    AddressDTO findById(int theId);

    Map<String, AddressDTO> findByShipmentId(int theId);

    AddressDTO findByShipmentsFromId(int theId);

    AddressDTO findByShipmentToId(int theId);

    AddressDTO findByWarehouseId(int theId);

}
