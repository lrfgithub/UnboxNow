package com.unboxnow.product.service;

import com.unboxnow.product.dto.BrandDTO;

import java.util.List;

public interface BrandService {

    List<BrandDTO> findAll();

    BrandDTO findById(int theId);

    BrandDTO create(BrandDTO dto);

    BrandDTO update(BrandDTO dto);

    BrandDTO partiallyUpdate(int theId, BrandDTO dto);

    void deleteById(int theId);
}
