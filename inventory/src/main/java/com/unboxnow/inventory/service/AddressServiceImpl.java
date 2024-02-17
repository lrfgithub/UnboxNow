package com.unboxnow.inventory.service;

import com.unboxnow.common.exception.NotFoundException;
import com.unboxnow.inventory.dao.AddressRepo;
import com.unboxnow.inventory.dao.ShipmentRepo;
import com.unboxnow.inventory.dto.AddressDTO;
import com.unboxnow.inventory.entity.Address;
import com.unboxnow.inventory.entity.Shipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepo addressRepo;

    private final ShipmentRepo shipmentRepo;

    @Autowired
    public AddressServiceImpl(AddressRepo addressRepo, ShipmentRepo shipmentRepo) {
        this.addressRepo = addressRepo;
        this.shipmentRepo = shipmentRepo;
    }

    @Override
    public List<AddressDTO> findAll() {
        List<Address> addresses = addressRepo.findAll();
        if (addresses.isEmpty()) return new ArrayList<>();
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
    public Map<String, AddressDTO> findByShipmentId(int theId) {
        Shipment shipment = shipmentRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Shipment", theId));
        Map<String, AddressDTO> addresses = new HashMap<>();
        addresses.put("departure", AddressDTO.fromEntity(shipment.getDepartureAddress()));
        addresses.put("destination", AddressDTO.fromEntity(shipment.getDestinationAddress()));
        return addresses;
    }

    @Override
    public AddressDTO findByShipmentsFromId(int theId) {
        Address address = addressRepo.findByShipmentsFromId(theId)
                .orElseThrow(() -> new NotFoundException("Address", "shipmentsFromId", theId));
        return AddressDTO.fromEntity(address);
    }

    @Override
    public AddressDTO findByShipmentToId(int theId) {
        Address address = addressRepo.findByShipmentToId(theId)
                .orElseThrow(() -> new NotFoundException("Address", "shipmentToId",theId));
        return AddressDTO.fromEntity(address);
    }

    @Override
    public AddressDTO findByWarehouseId(int theId) {
        Address address = addressRepo.findByWarehouseId(theId)
                .orElseThrow(() -> new NotFoundException("Address", "warehouseId", theId));
        return AddressDTO.fromEntity(address);
    }
}
