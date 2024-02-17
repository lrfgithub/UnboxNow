package com.unboxnow.inventory.controller;

import com.unboxnow.inventory.dto.InventoryDTO;
import com.unboxnow.inventory.service.InventoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventories")
@Validated
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<InventoryDTO> findAll() {
        return this.inventoryService.findAll();
    }

    @GetMapping("/{theId}")
    public InventoryDTO findById(@Min(1) @PathVariable int theId) {
        return this.inventoryService.findById(theId);
    }

    @GetMapping("/warehouse/{theId}")
    public List<InventoryDTO> findByWarehouseId(@Min(1) @PathVariable int theId) {
        return this.inventoryService.findByWarehouseId(theId);
    }

    @GetMapping("/sku/{sku}")
    public List<InventoryDTO> findBySku(@NotBlank @PathVariable String sku) {
        return this.inventoryService.findBySku(sku);
    }

    @PostMapping
    public InventoryDTO create(@Valid @RequestBody InventoryDTO dto) {
        return inventoryService.create(dto);
    }

    @PutMapping
    public InventoryDTO update(@Valid @RequestBody InventoryDTO dto) {
        return inventoryService.update(dto);
    }

    @PatchMapping("/{theId}")
    public InventoryDTO partiallyUpdate(@Min(0) @PathVariable int theId, @RequestBody InventoryDTO dto) {
        return inventoryService.partiallyUpdate(theId, dto);
    }

    @DeleteMapping("/{theId}")
    public String deleteById(@Min(1) @PathVariable int theId) {
        inventoryService.deleteById(theId);
        return "Inventory is deleted - " + theId;
    }
}
