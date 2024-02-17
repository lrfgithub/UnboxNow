package com.unboxnow.product.service;

import com.unboxnow.product.dto.DiscountDTO;

import java.util.List;

public interface DiscountService {

    List<DiscountDTO> findAll();

    DiscountDTO findById(int theId);

    DiscountDTO create(DiscountDTO dto);

    DiscountDTO update(DiscountDTO dto);

    DiscountDTO partiallyUpdate(int theId, DiscountDTO dto);

    void deleteById(int theId);
}
