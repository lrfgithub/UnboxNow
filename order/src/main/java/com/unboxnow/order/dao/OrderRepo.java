package com.unboxnow.order.dao;

import com.unboxnow.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order, Integer> {

    Optional<Order> findByAddressId(int theId);

    List<Order> findByMemberId(int theId);

}
