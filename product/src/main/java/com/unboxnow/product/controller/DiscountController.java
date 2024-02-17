package com.unboxnow.product.controller;

import com.unboxnow.product.dto.DiscountDTO;
import com.unboxnow.product.service.DiscountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/discounts")
public class DiscountController {

    private final DiscountService discountService;

    @Autowired
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping
    public List<DiscountDTO> findAll() {
        return discountService.findAll();
    }

    @GetMapping("/{theId}")
    public DiscountDTO findById(@Min(1) @PathVariable int theId) {
        return discountService.findById(theId);
    }

    @PostMapping
    public DiscountDTO create(@Valid @RequestBody DiscountDTO dto) {
        return discountService.create(dto);
    }

    @PutMapping
    public DiscountDTO update(@Valid @RequestBody DiscountDTO dto) {
        return discountService.update(dto);
    }

    @PatchMapping("/{theId}")
    public DiscountDTO partiallyUpdate(@Min(1) @PathVariable int theId, @RequestBody DiscountDTO dto) {
        return discountService.partiallyUpdate(theId, dto);
    }

    @DeleteMapping("/{theId}")
    public String deleteById(@Min(1) @PathVariable int theId) {
        this.discountService.deleteById(theId);
        return "Discount is deleted - " + theId;
    }
}
