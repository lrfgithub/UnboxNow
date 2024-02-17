package com.unboxnow.inventory.dao;

import com.unboxnow.inventory.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseRepo extends JpaRepository<Warehouse, Integer> {

    Optional<Warehouse> findByAddressId(int theId);
}
