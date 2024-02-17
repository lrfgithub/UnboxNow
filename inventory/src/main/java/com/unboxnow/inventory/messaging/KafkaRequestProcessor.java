package com.unboxnow.inventory.messaging;

import com.unboxnow.common.component.IdGenerator;
import com.unboxnow.common.constant.Topic;
import com.unboxnow.common.entity.InventoryItem;
import com.unboxnow.common.entity.QueryItem;
import com.unboxnow.common.entity.UniversalAddress;
import com.unboxnow.common.exception.IllegalMessageException;
import com.unboxnow.common.exception.IllegalValueException;
import com.unboxnow.common.message.*;
import com.unboxnow.inventory.dto.AddressDTO;
import com.unboxnow.inventory.dto.ShipmentDTO;
import com.unboxnow.inventory.dto.ShipmentItemDTO;
import com.unboxnow.inventory.service.AddressService;
import com.unboxnow.inventory.service.InventoryService;
import com.unboxnow.inventory.service.ShipmentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Validated
public class KafkaRequestProcessor {

    private final ZSetOperations<String, ContainerMessage<InventoryItem>> lockOps;

    private final InventoryService inventoryService;

    private final AddressService addressService;

    private final ShipmentService shipmentService;

    private final IdGenerator idGenerator;

    private final Producer producer;

    private final LocalDateTime base;

    @Autowired
    public KafkaRequestProcessor(RedisTemplate<String, ContainerMessage<InventoryItem>> lockRedisTemplate,
                                 InventoryService inventoryService,
                                 AddressService addressService,
                                 ShipmentService shipmentService,
                                 IdGenerator idGenerator,
                                 Producer producer) {
        this.lockOps = lockRedisTemplate.opsForZSet();
        this.inventoryService = inventoryService;
        this.addressService = addressService;
        this.shipmentService = shipmentService;
        this.idGenerator = idGenerator;
        this.producer = producer;
        this.base = LocalDateTime.of(2023, 11, 11, 0, 0, 0);
    }

    public void publishLock(@Valid ContainerMessage<InventoryItem> lock, @Min(0) double score) {
        lockOps.add(Topic.START_LOCKING.getName(), lock, score);
    }

    public void createLock(ContainerMessage<QueryItem> request) {
        ConfirmationMessage<QueryItem> response = new ConfirmationMessage<>(request.getEntityId());
        if (request.getItems() == null || request.getItems().isEmpty()) {
            response.setValid(false);
            response.setItems(request.getItems());
            producer.publish(response, Topic.LOCK_QUANTITY, request.getId());
            return;
        }
        ContainerMessage<InventoryItem> lock = new ContainerMessage<>(
                idGenerator.getId(Topic.START_LOCKING.getCounter()),
                Topic.START_LOCKING.getName(),
                request.getEntityId()
        );
        for (QueryItem item : request.getItems()) {
            List<InventoryItem> inventoryItems = inventoryService.deductQuantityByOrderItem(item);
            if (inventoryItems.isEmpty()) {
                response.add(item);
            } else {
                inventoryItems.forEach(lock::add);
            }
        }
        response.setValid(response.getItems() == null || response.getItems().isEmpty());
        if (lock.getItems() != null && !lock.getItems().isEmpty()) {
            double score = (double) Duration.between(base, LocalDateTime.now()).toMinutes();
            publishLock(lock, score);
        }
        producer.publish(response, Topic.LOCK_QUANTITY, request.getId());
    }

    @Scheduled(fixedDelay = 60000)
    public void unlockExpiredLocks() {
        int waitingTimeInMinutes = 30;
        LocalDateTime bar = LocalDateTime.now().minusMinutes(waitingTimeInMinutes);
        double maxScore = (double) Duration.between(base, bar).toMinutes();
        Set<ContainerMessage<InventoryItem>> locks =
                lockOps.rangeByScore(Topic.START_LOCKING.getName(), 0, maxScore);
        if (locks == null) {
            throw new IllegalValueException();
        } else if (locks.isEmpty()) {
            return;
        }
        locks.forEach(lock -> {
            if (lock.getItems() == null || lock.getItems().isEmpty()) {
                throw new IllegalMessageException(Topic.START_LOCKING.getName(), lock.getId(), "items");
            }
            lock.getItems().forEach(item ->
                    inventoryService.updateQuantityById(item.getInventoryId(), item.getQuantity()));
            producer.publish(new Message(lock.getEntityId()), Topic.STOP_LOCKING);
            lockOps.remove(Topic.START_LOCKING.getName(), lock);
        });

    }

    private Set<ContainerMessage<InventoryItem>> findAll() {
        String key = Topic.START_LOCKING.getName();
        Long size = lockOps.zCard(key);
        if (size == null) {
            throw new IllegalValueException("size");
        } else if (size < 1) {
            return new HashSet<>();
        }
        Set<ContainerMessage<InventoryItem>> locks = lockOps.range(key, 0, size);
        if (locks == null) {
            throw new IllegalValueException();
        }
        return locks;
    }

    private ContainerMessage<InventoryItem> findByOrderId(@Min(1) int orderId) {
        Set<ContainerMessage<InventoryItem>> locks = findAll();
        if (locks.isEmpty()) return null;
        for (ContainerMessage<InventoryItem> lock : locks) {
            if (lock.getEntityId() == orderId) {
                return lock;
            }
        }
        return null;
    }

    public void createShipments(CarrierMessage<UniversalAddress> request) {
        int orderId = request.getEntityId();
        ContainerMessage<InventoryItem> lock = findByOrderId(orderId);
        RetrieverMessage<Boolean> response = new RetrieverMessage<>(orderId);
        if (lock == null) {
            response.setValid(false);
            producer.publish(response, Topic.START_SHIPMENT, request.getId());
            return;
        } else if (lock.getItems() == null || lock.getItems().isEmpty()) {
            throw new IllegalMessageException(Topic.START_LOCKING.getName(), lock.getId(), "items");
        }
        response.setValid(true);
        AddressDTO destination = AddressDTO.fromEntity(request.getData());
        Map<Integer, ShipmentDTO> shipmentDTOMap = new HashMap<>();
        for (InventoryItem item : lock.getItems()) {
            Integer addressId = item.getAddressId();
            if (!shipmentDTOMap.containsKey(addressId)) {
                AddressDTO departure = addressService.findById(addressId);
                shipmentDTOMap.put(addressId, new ShipmentDTO(departure, destination, orderId));
            }
            shipmentDTOMap.get(addressId).add(ShipmentItemDTO.fromEntity(item));
        }
        for (ShipmentDTO dto : shipmentDTOMap.values()) {
            shipmentService.create(dto);
        }
        response.setData(true);
        producer.publish(response, Topic.START_SHIPMENT, request.getId());
        lockOps.remove(Topic.START_LOCKING.getName(), lock);
    }
}
