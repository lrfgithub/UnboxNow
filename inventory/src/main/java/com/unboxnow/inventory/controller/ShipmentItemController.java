package com.unboxnow.inventory.controller;

import com.unboxnow.inventory.dto.ShipmentItemDTO;
import com.unboxnow.inventory.service.ShipmentItemService;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shipment-items")
@Validated
public class ShipmentItemController {

    private final ShipmentItemService shipmentItemService;

    @Autowired
    public ShipmentItemController(ShipmentItemService shipmentItemService) {
        this.shipmentItemService = shipmentItemService;
    }

    @GetMapping
    public List<ShipmentItemDTO> findAll(){
        return shipmentItemService.findAll();
    }

    @GetMapping("/{theId}")
    public ShipmentItemDTO findById(@Min(1) @PathVariable int theId){
        return shipmentItemService.findById(theId);
    }

    @GetMapping("/shipment/{theId}")
    public List<ShipmentItemDTO> findByShipmentId(@Min(1) @PathVariable int theId){
        return shipmentItemService.findByShipmentId(theId);
    }

    @GetMapping("/sku/{sku}")
    public List<ShipmentItemDTO> findBySku(@NotBlank @PathVariable String sku){
        return shipmentItemService.findBySku(sku);
    }
}
