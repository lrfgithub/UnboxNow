package com.unboxnow.product.dao;

import com.unboxnow.product.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepo extends JpaRepository<Brand, Integer> {
}
