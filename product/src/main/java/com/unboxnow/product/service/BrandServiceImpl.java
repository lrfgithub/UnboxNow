package com.unboxnow.product.service;

import com.unboxnow.common.exception.NotFoundException;
import com.unboxnow.product.dao.BrandRepo;
import com.unboxnow.product.dao.ProductRepo;
import com.unboxnow.product.dto.BrandDTO;
import com.unboxnow.product.entity.Brand;
import com.unboxnow.product.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepo brandRepo;

    private final ProductRepo productRepo;

    @Autowired
    public BrandServiceImpl(BrandRepo brandRepo, ProductRepo productRepo) {
        this.brandRepo = brandRepo;
        this.productRepo = productRepo;
    }

    @Override
    public List<BrandDTO> findAll() {
        List<Brand> brands = brandRepo.findAll();
        if (brands.isEmpty()) return new ArrayList<>();
        return brands.stream()
                .map(BrandDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public BrandDTO findById(int theId) {
        Brand brand = brandRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Brand", theId));
        return BrandDTO.fromEntity(brand);
    }

    @Override
    public BrandDTO create(BrandDTO dto) {
        Brand brand = Brand.fromDTO(dto);
        brand.setId(0);
        return BrandDTO.fromEntity(brandRepo.save(brand));
    }

    @Override
    public BrandDTO update(BrandDTO dto) {
        int theId = dto.getId();
        Optional<Brand> res = brandRepo.findById(theId);
        if (res.isEmpty()) {
            throw new NotFoundException("Brand", theId);
        }
        Brand brand = Brand.fromDTO(dto);
        return BrandDTO.fromEntity(brandRepo.save(brand));
    }

    @Override
    public BrandDTO partiallyUpdate(int theId, BrandDTO dto) {
        Brand dbBrand = brandRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Brand", theId));

        String updateField = dto.getTitle();
        if (updateField != null) dbBrand.setTitle(updateField);

        updateField = dto.getDescription();
        if (updateField != null) dbBrand.setDescription(updateField);

        return BrandDTO.fromEntity(brandRepo.save(dbBrand));
    }

    @Override
    public void deleteById(int theId) {
        Optional<Brand> res = brandRepo.findById(theId);
        if (res.isEmpty()) {
            throw new NotFoundException("Brand", theId);
        } else {
            List<Product> products = productRepo.findByBrandId(theId);
            if (!products.isEmpty()) {
                products.forEach((product) -> {
                    product.setBrand(null);
                    productRepo.save(product);
                });
            }
            brandRepo.deleteById(theId);
        }
    }
}
