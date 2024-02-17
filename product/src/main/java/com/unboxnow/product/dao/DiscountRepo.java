package com.unboxnow.product.dao;

import com.unboxnow.product.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepo extends JpaRepository<Discount, Integer> {
}
