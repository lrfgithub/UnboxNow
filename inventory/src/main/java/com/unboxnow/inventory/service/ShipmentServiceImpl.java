package com.unboxnow.inventory.service;

import com.unboxnow.common.exception.NotFoundException;
import com.unboxnow.inventory.dao.ShipmentRepo;
import com.unboxnow.inventory.dto.ShipmentDTO;
import com.unboxnow.inventory.entity.Shipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShipmentServiceImpl implements ShipmentService {

    private final ShipmentRepo shipmentRepo;

    @Autowired
    public ShipmentServiceImpl(ShipmentRepo shipmentRepo) {
        this.shipmentRepo = shipmentRepo;
    }

    @Override
    public List<ShipmentDTO> findAll() {
        List<Shipment> shipments = shipmentRepo.findAll();
        if (shipments.isEmpty()) return new ArrayList<>();
        return shipments.stream()
                .map(ShipmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ShipmentDTO findById(int theId) {
        Shipment shipment = shipmentRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Shipment", theId));
        return ShipmentDTO.fromEntity(shipment);
    }

    @Override
    public List<ShipmentDTO> findByDepartureAddressId(int theId) {
        List<Shipment> shipments = shipmentRepo.findByDepartureAddressId(theId);
        return shipments.stream()
                .map(ShipmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ShipmentDTO findByDestinationAddressId(int theId) {
        Shipment shipment = shipmentRepo.findByDestinationAddressId(theId)
                .orElseThrow(() -> new NotFoundException("Shipment", "destinationAddressId", theId));
        return ShipmentDTO.fromEntity(shipment);
    }

    @Override
    public ShipmentDTO create(ShipmentDTO dto) {
        Shipment shipment = Shipment.fromDTO(dto);
        shipment.setId(0);
        shipment.getDepartureAddress().setId(0);
        shipment.getDestinationAddress().setId(0);
        shipment.getShipmentItems().forEach(item -> item.setShipment(shipment));
        shipment.update();
        return ShipmentDTO.fromEntity(shipmentRepo.save(shipment));
    }

    @Override
    public void deleteById(int theId) {
        Optional<Shipment> opt = shipmentRepo.findById(theId);
        if (opt.isEmpty()) {
            throw new NotFoundException("Shipment", theId);
        }
        shipmentRepo.deleteById(theId);
    }
}
