package com.unboxnow.inventory.dao;

import com.unboxnow.inventory.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShipmentRepo extends JpaRepository<Shipment, Integer> {

    List<Shipment> findByDepartureAddressId(int theId);

    Optional<Shipment> findByDestinationAddressId(int theId);
}
