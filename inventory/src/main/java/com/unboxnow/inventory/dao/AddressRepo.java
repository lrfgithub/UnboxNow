package com.unboxnow.inventory.dao;

import com.unboxnow.inventory.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepo extends JpaRepository<Address, Integer> {

    Optional<Address> findByShipmentToId(int theId);

    Optional<Address> findByShipmentsFromId(int theId);

    Optional<Address> findByWarehouseId(int theId);
}
