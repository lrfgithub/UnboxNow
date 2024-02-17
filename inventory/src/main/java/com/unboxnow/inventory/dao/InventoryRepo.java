package com.unboxnow.inventory.dao;

import com.unboxnow.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepo extends JpaRepository<Inventory, Integer> {

    List<Inventory> findByWarehouseId(int theId);

    List<Inventory> findBySku(String sku);
}
