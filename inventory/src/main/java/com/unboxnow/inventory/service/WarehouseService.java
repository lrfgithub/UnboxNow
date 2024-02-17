package com.unboxnow.inventory.service;

import com.unboxnow.inventory.dto.WarehouseDTO;

import java.util.List;

public interface WarehouseService {

    List<WarehouseDTO> findAll();

    WarehouseDTO findById(int theId);

    WarehouseDTO findByAddressId(int theId);

    WarehouseDTO create(WarehouseDTO dto);

    WarehouseDTO update(WarehouseDTO dto);

    WarehouseDTO partiallyUpdate(int theId, WarehouseDTO dto);

    void deleteById(int theId);
}
