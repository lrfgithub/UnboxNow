package com.unboxnow.product.service;

import com.unboxnow.common.exception.NotFoundException;
import com.unboxnow.product.dao.CategoryRepo;
import com.unboxnow.product.dao.ProductRepo;
import com.unboxnow.product.dto.CategoryDTO;
import com.unboxnow.product.entity.Category;
import com.unboxnow.product.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;

    private final ProductRepo productRepo;

    @Autowired
    public CategoryServiceImpl(CategoryRepo categoryRepo, ProductRepo productRepo) {
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
    }

    @Override
    public List<CategoryDTO> findAll() {
        List<Category> categories = categoryRepo.findAll();
        if (categories.isEmpty()) return new ArrayList<>();
        return categories.stream()
                .map(CategoryDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDTO findById(int theId) {
        Category category = categoryRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Category", theId));
        return CategoryDTO.fromEntity(category);
    }

    @Override
    public CategoryDTO create(CategoryDTO dto) {
        Category category = Category.fromDTO(dto);
        category.setId(0);
        return CategoryDTO.fromEntity(categoryRepo.save(category));
    }

    @Override
    public CategoryDTO update(CategoryDTO dto) {
        int theId = dto.getId();
        Optional<Category> res = categoryRepo.findById(theId);
        if (res.isEmpty()) {
            throw new NotFoundException("Category", theId);
        }
        Category category = Category.fromDTO(dto);
        return CategoryDTO.fromEntity(categoryRepo.save(category));
    }

    @Override
    public CategoryDTO partiallyUpdate(int theId, CategoryDTO dto) {
        Category dbCategory = categoryRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Category", theId));

        String updateField = dto.getTitle();
        if (updateField != null) dbCategory.setTitle(updateField);

        updateField = dto.getDescription();
        if (updateField != null) dbCategory.setDescription(updateField);

        return CategoryDTO.fromEntity(categoryRepo.save(dbCategory));
    }

    @Override
    public void deleteById(int theId) {
        Optional<Category> res = categoryRepo.findById(theId);
        if (res.isEmpty()) {
            throw new NotFoundException("Category", theId);
        }
        List<Product> products = productRepo.findByCategoryId(theId);
        if (!products.isEmpty()) {
            products.forEach((product) -> {
                product.setCategory(null);
                productRepo.save(product);
            });
        }
        categoryRepo.deleteById(theId);
    }
}
