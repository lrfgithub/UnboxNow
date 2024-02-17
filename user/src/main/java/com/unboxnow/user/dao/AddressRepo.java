package com.unboxnow.user.dao;

import com.unboxnow.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepo extends JpaRepository<Address, Integer> {

    List<Address> findByMemberId(int theId);
}
