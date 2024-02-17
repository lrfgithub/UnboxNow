package com.unboxnow.order.service;

import com.unboxnow.common.exception.NotFoundException;
import com.unboxnow.order.dao.AddressRepo;
import com.unboxnow.order.dto.AddressDTO;
import com.unboxnow.order.entity.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepo addressRepo;

    @Autowired
    public AddressServiceImpl(AddressRepo addressRepo) {
        this.addressRepo = addressRepo;
    }

    @Override
    public List<AddressDTO> findAll() {
        List<Address> addresses = addressRepo.findAll();
        return addresses.stream()
                .map(AddressDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public AddressDTO findById(int theId) {
        Address address = addressRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Address", theId));
        return AddressDTO.fromEntity(address);
    }

    @Override
    public AddressDTO findByOrderId(int theId) {
        Address address = addressRepo.findByOrderId(theId)
                .orElseThrow(() -> new NotFoundException("Address", "orderId", theId));
        return AddressDTO.fromEntity(address);
    }
}
