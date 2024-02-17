package com.unboxnow.product.controller;

import UnboxNow.product.dto.CategoryDTO;
import UnboxNow.product.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDTO> findAll() {
        return categoryService.findAll();
    }

    @GetMapping("/{theId}")
    public CategoryDTO findById(@Min(1) @PathVariable int theId) {
        return categoryService.findById(theId);
    }

    @PostMapping
    public CategoryDTO create(@Valid @RequestBody CategoryDTO dto) {
        return categoryService.create(dto);
    }

    @PutMapping
    public CategoryDTO update(@Valid @RequestBody CategoryDTO dto) {
        return categoryService.update(dto);
    }

    @PatchMapping("/{theId}")
    public CategoryDTO partiallyUpdate(@Min(1) @PathVariable int theId, @RequestBody CategoryDTO dto) {
        return categoryService.partiallyUpdate(theId, dto);
    }

    @DeleteMapping("/{theId}")
    public String deleteById(@Min(1) @PathVariable int theId) {
        this.categoryService.deleteById(theId);
        return "Category is deleted - " + theId;
    }
}
