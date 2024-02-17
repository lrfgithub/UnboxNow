package com.unboxnow.inventory.controller;

import com.unboxnow.inventory.dto.ShipmentDTO;
import com.unboxnow.inventory.service.ShipmentService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shipments")
@Validated
public class ShipmentController {

    private final ShipmentService shipmentService;

    @Autowired
    public ShipmentController(ShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    @GetMapping
    public List<ShipmentDTO> findAll() {
        return shipmentService.findAll();
    }

    @GetMapping("/{theId}")
    public ShipmentDTO findById(@Min(1) @PathVariable int theId) {
        return shipmentService.findById(theId);
    }

    @GetMapping("/departure/{theId}")
    public List<ShipmentDTO> findByDepartureAddressId(@Min(1) @PathVariable int theId) {
        return shipmentService.findByDepartureAddressId(theId);
    }

    @GetMapping("/destination/{theId}")
    public ShipmentDTO findByDestinationAddressId(@Min(1) @PathVariable int theId) {
        return shipmentService.findByDestinationAddressId(theId);
    }

    @DeleteMapping("/{theId}")
    public String deleteById(@Min(1) @PathVariable int theId) {
        shipmentService.deleteById(theId);
        return "Shipment is deleted - " + theId;
    }
}
