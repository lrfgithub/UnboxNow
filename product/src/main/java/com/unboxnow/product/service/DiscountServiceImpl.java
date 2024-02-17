package com.unboxnow.product.service;

import com.unboxnow.common.constant.Topic;
import com.unboxnow.common.exception.ActiveDiscountException;
import com.unboxnow.common.exception.ApplicableException;
import com.unboxnow.common.exception.NotFoundException;
import com.unboxnow.common.message.Message;
import com.unboxnow.product.dao.DiscountRepo;
import com.unboxnow.product.dao.ProductRepo;
import com.unboxnow.product.dto.DiscountDTO;
import com.unboxnow.product.entity.Discount;
import com.unboxnow.product.entity.Product;
import com.unboxnow.product.messaging.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepo discountRepo;

    private final ProductRepo productRepo;

    private final Producer producer;

    @Autowired
    public DiscountServiceImpl(DiscountRepo discountRepo,
                               ProductRepo productRepo,
                               Producer producer) {
        this.discountRepo = discountRepo;
        this.productRepo = productRepo;
        this.producer = producer;
    }

    @Override
    public List<DiscountDTO> findAll() {
        List<Discount> discounts = discountRepo.findAll();
        if (discounts.isEmpty()) return new ArrayList<>();
        return discounts.stream()
                .map(DiscountDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public DiscountDTO findById(int theId) {
        Discount discount = discountRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Discount", theId));
        return DiscountDTO.fromEntity(discount);
    }

    @Override
    public DiscountDTO create(DiscountDTO dto) {
        Discount discount = Discount.fromDTO(dto);
        discount.setId(0);
        return DiscountDTO.fromEntity(discountRepo.save(discount));
    }

    @Override
    public DiscountDTO update(DiscountDTO dto) {
        int theId = dto.getId();
        Discount dbDiscount = discountRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Discount", theId));
        Discount discount = Discount.fromDTO(dto);
        DiscountDTO res = DiscountDTO.fromEntity(discountRepo.save(discount));
        publishIfUpdated(DiscountDTO.fromEntity(dbDiscount), res);
        return res;
    }

    @Override
    public DiscountDTO partiallyUpdate(int theId, DiscountDTO dto) {
        Discount dbDiscount = discountRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Discount", theId));

        DiscountDTO prev = DiscountDTO.fromEntity(dbDiscount);

        String updateField = dto.getTitle();
        if (updateField != null) {
            dbDiscount.setTitle(updateField);
        }

        updateField = dto.getDescription();
        if (updateField != null) {
            dbDiscount.setDescription(updateField);
        }

        BigDecimal updatePercent = dto.getPercent();
        if (updatePercent != null) {
            dbDiscount.setPercent(updatePercent);
        }

        Boolean updateActive = dto.getActive();
        if (updateActive != null) {
            dbDiscount.setActive(updateActive);
        }

        DiscountDTO res = DiscountDTO.fromEntity(discountRepo.save(dbDiscount));

        publishIfUpdated(prev, res);

        return res;
    }

    @Override
    public void deleteById(int theId) {
        Discount discount = discountRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Discount", theId));
        if (discount.getActive()) {
            throw new ActiveDiscountException(theId);
        }
        List<Product> products = productRepo.findByDiscountId(theId);
        if (!products.isEmpty()) {
            throw new ApplicableException("Discount", theId, "Product");
        }
        discountRepo.deleteById(theId);
    }

    private void publishIfUpdated(DiscountDTO prev, DiscountDTO curr) {
        if (!prev.getActive() && !curr.getActive()) return;
        boolean updated = prev.getActive() != curr.getActive();
        if (!updated) {
            updated = !prev.getPercent().equals(curr.getPercent());
        }
        if (updated) {
            List<Product> products = productRepo.findByDiscountId(curr.getId());
            if (!products.isEmpty()) {
                products.forEach(product -> producer.publish(new Message(product.getId()), Topic.UPDATE_PRODUCT));
            }
        }
    }
}
