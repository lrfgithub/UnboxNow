package com.unboxnow.order.dao;

import com.unboxnow.order.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepo extends JpaRepository<Address, Integer> {

    Optional<Address> findByOrderId(int theId);

}
