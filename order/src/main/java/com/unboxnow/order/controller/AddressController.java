package com.unboxnow.order.controller;

import com.unboxnow.order.dto.AddressDTO;
import com.unboxnow.order.service.AddressService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order-addresses")
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

    @GetMapping("/order/{theId}")
    public AddressDTO findByOrderId(@Min(1) @PathVariable int theId){
        return addressService.findByOrderId(theId);
    }
}
