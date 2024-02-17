package com.unboxnow.inventory.dao;

import com.unboxnow.inventory.entity.ShipmentItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShipmentItemRepo extends JpaRepository<ShipmentItem, Integer> {

    List<ShipmentItem> findByShipmentId(int theId);

    List<ShipmentItem> findBySku(String sku);
}
