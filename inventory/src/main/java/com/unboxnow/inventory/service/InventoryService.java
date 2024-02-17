package com.unboxnow.inventory.service;

import com.unboxnow.common.entity.InventoryItem;
import com.unboxnow.common.entity.QueryItem;
import com.unboxnow.inventory.dto.InventoryDTO;

import java.util.List;

public interface InventoryService {

    List<InventoryDTO> findAll();

    InventoryDTO findById(int theId);

    List<InventoryDTO> findByWarehouseId(int theId);

    List<InventoryDTO> findBySku(String sku);

    boolean checkQuantityBySku(String sku, int quantity);

    InventoryDTO create(InventoryDTO dto);

    InventoryDTO update(InventoryDTO dto);

    InventoryDTO partiallyUpdate(int theId, InventoryDTO dto);

    InventoryDTO updateQuantityById(int theId, int delta);

    List<InventoryItem> deductQuantityByOrderItem(QueryItem queryItem);

    void deleteById(int theId);

}
