package com.unboxnow.inventory.controller;

import com.unboxnow.inventory.dto.WarehouseDTO;
import com.unboxnow.inventory.service.WarehouseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouses")
@Validated
public class WarehouseController {

    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping
    public List<WarehouseDTO> findAll() {
        return this.warehouseService.findAll();
    }

    @GetMapping("/{theId}")
    public WarehouseDTO findById(@Min(1) @PathVariable int theId) {
        return this.warehouseService.findById(theId);
    }

    @GetMapping("/address/{theId}")
    public WarehouseDTO findByAddressId(@Min(1) @PathVariable int theId) {
        return this.warehouseService.findByAddressId(theId);
    }

    @PostMapping
    public WarehouseDTO create(@Valid @RequestBody WarehouseDTO dto) {
        return warehouseService.create(dto);
    }

    @PutMapping
    public WarehouseDTO update(@Valid @RequestBody WarehouseDTO dto) {
        return warehouseService.update(dto);
    }

    @PatchMapping("/{theId}")
    public WarehouseDTO partiallyUpdate(@Min(1) @PathVariable int theId, @RequestBody WarehouseDTO dto) {
        return warehouseService.partiallyUpdate(theId, dto);
    }

    @DeleteMapping("/{theId}")
    public String deleteById(@Min(1) @PathVariable int theId) {
        warehouseService.deleteById(theId);
        return "Warehouse is deleted - " + theId;
    }
}
