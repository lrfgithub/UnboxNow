package com.unboxnow.product.controller;

import com.unboxnow.product.dto.BrandDTO;
import com.unboxnow.product.service.BrandService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
@Validated
public class BrandController {

    private final BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public List<BrandDTO> findAll() {
        return brandService.findAll();
    }

    @GetMapping("/{theId}")
    public BrandDTO findById(@Min(1) @PathVariable int theId) {
        return brandService.findById(theId);
    }

    @PostMapping
    public BrandDTO create(@Valid @RequestBody BrandDTO dto) {
        return brandService.create(dto);
    }

    @PutMapping
    public BrandDTO update(@Valid @RequestBody BrandDTO dto) {
        return brandService.update(dto);
    }

    @PatchMapping("/{theId}")
    public BrandDTO partiallyUpdate(@Min(1) @PathVariable int theId, @RequestBody BrandDTO dto) {
        return brandService.partiallyUpdate(theId, dto);
    }

    @DeleteMapping("/{theId}")
    public String deleteById(@Min(1) @PathVariable int theId) {
        this.brandService.deleteById(theId);
        return "Brand is deleted - " + theId;
    }
}
