package com.unboxnow.inventory.service;

import com.unboxnow.common.exception.ApplicableException;
import com.unboxnow.common.exception.NotFoundException;
import com.unboxnow.inventory.dao.AddressRepo;
import com.unboxnow.inventory.dao.InventoryRepo;
import com.unboxnow.inventory.dao.WarehouseRepo;
import com.unboxnow.inventory.dto.WarehouseDTO;
import com.unboxnow.inventory.entity.Address;
import com.unboxnow.inventory.entity.Inventory;
import com.unboxnow.inventory.entity.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepo warehouseRepo;

    private final InventoryRepo inventoryRepo;

    private final AddressRepo addressRepo;

    @Autowired
    public WarehouseServiceImpl(WarehouseRepo warehouseRepo, InventoryRepo inventoryRepo, AddressRepo addressRepo) {
        this.warehouseRepo = warehouseRepo;
        this.inventoryRepo = inventoryRepo;
        this.addressRepo = addressRepo;
    }

    @Override
    public List<WarehouseDTO> findAll() {
        List<Warehouse> warehouses = warehouseRepo.findAll();
        if (warehouses.isEmpty()) {
            return new ArrayList<>();
        }
        return warehouses.stream()
                .map(WarehouseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public WarehouseDTO findById(int theId) {
        Warehouse warehouse = warehouseRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Warehouse", theId));
        return WarehouseDTO.fromEntity(warehouse);
    }

    @Override
    public WarehouseDTO findByAddressId(int theId) {
        Warehouse warehouse = warehouseRepo.findByAddressId(theId)
                .orElseThrow(() -> new NotFoundException("Warehouse", theId));
        return WarehouseDTO.fromEntity(warehouse);
    }

    @Override
    public WarehouseDTO create(WarehouseDTO dto) {
        Warehouse warehouse = Warehouse.fromDTO(dto);
        warehouse.setId(0);
        warehouse.getAddress().setId(0);
        warehouse.getAddress().setWarehouse(warehouse);
        return WarehouseDTO.fromEntity(warehouseRepo.save(warehouse));
    }

    @Override
    public WarehouseDTO update(WarehouseDTO dto) {
        int theId = dto.getId();
        Warehouse dbWarehouse = warehouseRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Warehouse", theId));
        Address dbAddress = dbWarehouse.getAddress();
        Warehouse warehouse = Warehouse.fromDTO(dto);
        if (dto.getAddressId() == 0) {
            warehouse.getAddress().setWarehouse(warehouse);
        } else if (dto.getAddressId() != dbAddress.getId()) {
            Optional<Address> opt = addressRepo.findById(dto.getAddressId());
            warehouse.setAddress(opt.orElse(dbAddress));
        }
        return WarehouseDTO.fromEntity(warehouseRepo.save(warehouse));
    }

    @Override
    public WarehouseDTO partiallyUpdate(int theId, WarehouseDTO dto) {
        Warehouse dbWarehouse = warehouseRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Warehouse", theId));
        Address dbAddress = dbWarehouse.getAddress();

        String updateField = dto.getTitle();
        if (updateField != null) dbWarehouse.setTitle(updateField);

        updateField = dto.getContact();
        if (updateField != null) dbWarehouse.setContact(updateField);

        updateField = dto.getTelephone();
        if (updateField != null) dbWarehouse.setTelephone(updateField);

        updateField = dto.getEmail();
        if (updateField != null) dbWarehouse.setEmail(updateField);

        if (dto.getAddressId() == 0) {
            updateField = dto.getAddressName();
            if (updateField != null) dbWarehouse.getAddress().setName(updateField);

            updateField = dto.getAddressMobile();
            if (updateField != null) dbWarehouse.getAddress().setMobile(updateField);

            updateField = dto.getAddressEmail();
            if (updateField != null) dbWarehouse.getAddress().setEmail(updateField);

            updateField = dto.getAddress1();
            if (updateField != null) dbWarehouse.getAddress().setAddress1(updateField);

            updateField = dto.getAddress2();
            if (updateField != null) dbWarehouse.getAddress().setAddress2(updateField);

            updateField = dto.getCity();
            if (updateField != null) dbWarehouse.getAddress().setCity(updateField);

            updateField = dto.getState();
            if (updateField != null) dbWarehouse.getAddress().setState(updateField);

            updateField = dto.getZip();
            if (updateField != null) dbWarehouse.getAddress().setZip(updateField);
        } else if (dto.getAddressId() != dbAddress.getId()) {
            Optional<Address> opt = addressRepo.findById(dto.getAddressId());
            opt.ifPresent(dbWarehouse::setAddress);
        }

        return WarehouseDTO.fromEntity(warehouseRepo.save(dbWarehouse));
    }

    @Override
    public void deleteById(int theId) {
        Warehouse warehouse = warehouseRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Warehouse", theId));
        List<Inventory> inventories = inventoryRepo.findByWarehouseId(theId);
        if (!inventories.isEmpty()) {
            throw new ApplicableException("Warehouse", theId, "Inventory");
        }
        warehouse.getAddress().setId(0);
        warehouseRepo.save(warehouse);
        warehouseRepo.deleteById(theId);
    }
}
