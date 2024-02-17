package com.unboxnow.product.service;

import com.unboxnow.product.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    List<ProductDTO> findAll();

    ProductDTO findById(int theId);

    List<ProductDTO> findByCategoryId(int theId);

    List<ProductDTO> findByBrandId(int theId);

    List<ProductDTO> findByDiscountId(int theId);

    List<ProductDTO> findBySku(String sku);

    ProductDTO create(ProductDTO dto);

    ProductDTO update(ProductDTO dto);

    ProductDTO partiallyUpdate(int theId, ProductDTO dto);

    void deleteById(int theId);
}
