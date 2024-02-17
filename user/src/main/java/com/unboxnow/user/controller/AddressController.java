package com.unboxnow.user.controller;

import com.unboxnow.user.dto.AddressDTO;
import com.unboxnow.user.service.AddressService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-addresses")
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

    @GetMapping("/member/{theId}")
    public List<AddressDTO> findByMemberId(@Min(1) @PathVariable int theId){
        return addressService.findByMemberId(theId);
    }

    @PostMapping
    public AddressDTO create(@Valid @RequestBody AddressDTO dto) {
        return addressService.create(dto);
    }

    @PutMapping
    public AddressDTO update(@Valid @RequestBody AddressDTO dto) {
        return addressService.update(dto);
    }

    @DeleteMapping("/{theId}")
    public String deleteById(@Min(1) @PathVariable int theId) {
        addressService.deleteById(theId);
        return "Address is deleted - " + theId;
    }
}
