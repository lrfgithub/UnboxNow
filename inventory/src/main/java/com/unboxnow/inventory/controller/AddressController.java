package com.unboxnow.inventory.controller;

import com.unboxnow.inventory.dto.AddressDTO;
import com.unboxnow.inventory.service.AddressService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inventory-addresses")
@Validated
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public List<AddressDTO> findAll(){
        return addressService.findAll();
    }

    @GetMapping("/{theId}")
    public AddressDTO findById(@Min(1) @PathVariable int theId){
        return addressService.findById(theId);
    }

    @GetMapping("/shipment/{theId}")
    public Map<String, AddressDTO> findByShipmentId(@Min(1) @PathVariable int theId) {
        return addressService.findByShipmentId(theId);
    }

    @GetMapping("/shipment-from/{theId}")
    public AddressDTO findByShipmentsFromId(@Min(1) @PathVariable int theId){
        return addressService.findByShipmentsFromId(theId);
    }

    @GetMapping("/shipment-to/{theId}")
    public AddressDTO findByShipmentToId(@Min(1) @PathVariable int theId){
        return addressService.findByShipmentToId(theId);
    }

    @GetMapping("/warehouse/{theId}")
    public AddressDTO findByWarehouseId(@Min(1) @PathVariable int theId){
        return addressService.findByWarehouseId(theId);
    }
}
