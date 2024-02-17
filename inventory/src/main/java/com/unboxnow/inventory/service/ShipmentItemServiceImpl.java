package com.unboxnow.inventory.service;

import com.unboxnow.common.exception.NotFoundException;
import com.unboxnow.inventory.dao.ShipmentItemRepo;
import com.unboxnow.inventory.dto.ShipmentItemDTO;
import com.unboxnow.inventory.entity.ShipmentItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentItemServiceImpl implements ShipmentItemService {

    private final ShipmentItemRepo shipmentItemRepo;

    @Autowired
    public ShipmentItemServiceImpl(ShipmentItemRepo shipmentItemRepo) {
        this.shipmentItemRepo = shipmentItemRepo;
    }

    @Override
    public List<ShipmentItemDTO> findAll() {
        return ShipmentItemDTO.fromEntities(shipmentItemRepo.findAll());
    }

    @Override
    public ShipmentItemDTO findById(int theId) {
        ShipmentItem item = shipmentItemRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("ShipmentItem", theId));
        return ShipmentItemDTO.fromEntity(item);
    }

    @Override
    public List<ShipmentItemDTO> findByShipmentId(int theId) {
        return ShipmentItemDTO.fromEntities(shipmentItemRepo.findByShipmentId(theId));
    }

    @Override
    public List<ShipmentItemDTO> findBySku(String sku) {
        return ShipmentItemDTO.fromEntities(shipmentItemRepo.findBySku(sku));
    }
}
