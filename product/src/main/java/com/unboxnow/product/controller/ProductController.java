package com.unboxnow.product.controller;

import UnboxNow.product.dto.ProductDTO;
import UnboxNow.product.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Validated
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDTO> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{theId}")
    public ProductDTO findById(@Min(1) @PathVariable int theId) {
        return productService.findById(theId);
    }

    @GetMapping("/brand/{theId}")
    public List<ProductDTO> findByBrandId(@Min(1) @PathVariable int theId) {
        return productService.findByBrandId(theId);
    }

    @GetMapping("/category/{theId}")
    public List<ProductDTO> findByCategoryId(@Min(1) @PathVariable int theId) {
        return productService.findByCategoryId(theId);
    }

    @GetMapping("/discount/{theId}")
    public List<ProductDTO> findByDiscountId(@Min(1) @PathVariable int theId) {
        return productService.findByDiscountId(theId);
    }

    @GetMapping("/sku/{sku}")
    public List<ProductDTO> findBySku(@NotBlank @PathVariable String sku) {
        return productService.findBySku(sku);
    }

    @PostMapping
    public ProductDTO create(@Valid @RequestBody ProductDTO dto) {
        return productService.create(dto);
    }

    @PutMapping
    public ProductDTO update(@Valid @RequestBody ProductDTO dto) {
        return productService.update(dto);
    }

    @PatchMapping("/{theId}")
    public ProductDTO partiallyUpdate(@Min(1) @PathVariable int theId, @RequestBody ProductDTO dto) {
        return productService.partiallyUpdate(theId, dto);
    }

    @DeleteMapping("/{theId}")
    public String deleteById(@Min(1) @PathVariable int theId) {
        this.productService.deleteById(theId);
        return "Product is deleted - " + theId;
    }
}
