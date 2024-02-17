package com.unboxnow.product.dao;

import com.unboxnow.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Integer> {

    List<Product> findBySku(String sku);

    List<Product> findByBrandId(int theId);

    List<Product> findByCategoryId(int theId);

    List<Product> findByDiscountId(int theId);
}
