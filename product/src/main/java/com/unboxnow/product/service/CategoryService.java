package com.unboxnow.product.service;

import com.unboxnow.product.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> findAll();

    CategoryDTO findById(int theId);

    CategoryDTO create(CategoryDTO dto);

    CategoryDTO update(CategoryDTO dto);

    CategoryDTO partiallyUpdate(int theId, CategoryDTO dto);

    void deleteById(int theId);
}
