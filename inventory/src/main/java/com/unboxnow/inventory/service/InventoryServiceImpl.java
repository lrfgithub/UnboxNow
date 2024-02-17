package com.unboxnow.inventory.service;

import com.unboxnow.common.entity.InventoryItem;
import com.unboxnow.common.entity.QueryItem;
import com.unboxnow.common.exception.MinQuantityException;
import com.unboxnow.common.exception.NotFoundException;
import com.unboxnow.inventory.dao.InventoryRepo;
import com.unboxnow.inventory.dao.WarehouseRepo;
import com.unboxnow.inventory.dto.InventoryDTO;
import com.unboxnow.inventory.entity.Inventory;
import com.unboxnow.inventory.entity.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepo inventoryRepo;

    private final WarehouseRepo warehouseRepo;

    @Autowired
    public InventoryServiceImpl(InventoryRepo inventoryRepo, WarehouseRepo warehouseRepo) {
        this.inventoryRepo = inventoryRepo;
        this.warehouseRepo = warehouseRepo;
    }

    @Override
    public List<InventoryDTO> findAll() {
        return InventoryDTO.fromEntities(inventoryRepo.findAll());
    }

    @Override
    public InventoryDTO findById(int theId) {
        Inventory inventory = inventoryRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Inventory", theId));
        return InventoryDTO.fromEntity(inventory);
    }

    @Override
    public List<InventoryDTO> findByWarehouseId(int theId) {
        return InventoryDTO.fromEntities(inventoryRepo.findByWarehouseId(theId));
    }

    @Override
    public List<InventoryDTO> findBySku(String sku) {
        return InventoryDTO.fromEntities(inventoryRepo.findBySku(sku));
    }

    @Override
    public boolean checkQuantityBySku(String sku, int quantity) {
        List<Inventory> inventories = inventoryRepo.findBySku(sku);
        if (inventories.isEmpty()) {
            return false;
        }
        int stock = -quantity;
        for (Inventory inventory : inventories) {
            stock += inventory.getQuantity();
            if (stock >= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public InventoryDTO create(InventoryDTO dto) {
        int warehouseId = dto.getWarehouseId();
        if (warehouseId == 0) {
            throw new NotFoundException("Warehouse", 0);
        }
        Warehouse warehouse = warehouseRepo.findById(warehouseId)
                .orElseThrow(() -> new NotFoundException("Warehouse", warehouseId));
        Inventory inventory = Inventory.fromDTO(dto);
        inventory.setId(0);
        inventory.setWarehouse(null);
        Inventory dbInventory = inventoryRepo.save(inventory);
        dbInventory.setWarehouse(warehouse);
        return InventoryDTO.fromEntity(inventoryRepo.save(dbInventory));
    }

    @Override
    public InventoryDTO update(InventoryDTO dto) {
        int theId = dto.getId();
        Inventory dbInventory = inventoryRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Inventory", theId));
        Inventory inventory = Inventory.fromDTO(dto);
        int warehouseId = dto.getWarehouseId();
        if (warehouseId == 0) {
            throw new NotFoundException("Warehouse", warehouseId);
        } else {
            Warehouse warehouse;
            if (warehouseId == dbInventory.getWarehouse().getId()) {
                warehouse = dbInventory.getWarehouse();
            } else {
                warehouse = warehouseRepo.findById(warehouseId)
                        .orElseThrow(() -> new NotFoundException("Warehouse", warehouseId));
            }
            inventory.setWarehouse(warehouse);
        }
        return InventoryDTO.fromEntity(inventoryRepo.save(inventory));
    }

    @Override
    public InventoryDTO partiallyUpdate(int theId, InventoryDTO dto) {
        Inventory dbInventory = inventoryRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Inventory", theId));

        String updateField = dto.getTitle();
        if (updateField != null) dbInventory.setTitle(updateField);

        updateField = dto.getSku();
        if (updateField != null) dbInventory.setSku(updateField);

        int warehouseId = dto.getWarehouseId();
        if (warehouseId != 0 && warehouseId != dbInventory.getWarehouse().getId()) {
            Warehouse warehouse = warehouseRepo.findById(warehouseId)
                    .orElseThrow(() -> new NotFoundException("Warehouse", warehouseId));
            dbInventory.setWarehouse(warehouse);
        }

        Integer updateQuantity = dto.getQuantity();
        if (updateQuantity != null) {
            if (updateQuantity < 0) {
                throw new MinQuantityException("Inventory", updateQuantity, 0);
            }
            dbInventory.setQuantity(updateQuantity);
        }

        return InventoryDTO.fromEntity(inventoryRepo.save(dbInventory));
    }

    @Override
    public InventoryDTO updateQuantityById(int theId, int delta) {
        Inventory dbInventory = inventoryRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Inventory", theId));
        int update = dbInventory.getQuantity() + delta;
        if (update < 0) {
            throw new MinQuantityException("Inventory", update, 0);
        }
        dbInventory.setQuantity(update);
        return InventoryDTO.fromEntity(inventoryRepo.save(dbInventory));
    }

    @Override
    public List<InventoryItem> deductQuantityByOrderItem(QueryItem queryItem) {
        List<InventoryItem> res = new ArrayList<>();
        if (queryItem == null || queryItem.getSku() == null) return res;
        List<Inventory> inventories = inventoryRepo.findBySku(queryItem.getSku());
        if (inventories.isEmpty()) return res;
        inventories.sort((i1, i2) -> i2.getQuantity() - i1.getQuantity());
        int totalStock = inventories.stream()
                .map(Inventory::getQuantity)
                .mapToInt(Integer::intValue)
                .sum();
        int demand = queryItem.getQuantity();
        if (totalStock < demand) return res;
        for (Inventory inventory : inventories) {
            int stock = inventory.getQuantity();
            int inventoryItemQuantity = Math.min(stock, demand);
            InventoryItem inventoryItem = InventoryItem.fromOrderItem(
                    queryItem,
                    inventoryItemQuantity,
                    inventory.getId(),
                    inventory.getWarehouse().getAddress().getId()
            );
            inventory.setQuantity(stock - inventoryItemQuantity);
            demand -= inventoryItemQuantity;
            inventoryRepo.save(inventory);
            res.add(inventoryItem);
            if (demand == 0) break;
        }
        return res;
    }

    @Override
    public void deleteById(int theId) {
        Optional<Inventory> res = inventoryRepo.findById(theId);
        if (res.isEmpty()) {
            throw new NotFoundException("Inventory", theId);
        }
        inventoryRepo.deleteById(theId);
    }
}
