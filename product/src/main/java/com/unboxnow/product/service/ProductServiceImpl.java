package com.unboxnow.product.service;

import com.unboxnow.common.constant.Topic;
import com.unboxnow.common.exception.NotFoundException;
import com.unboxnow.common.message.Message;
import com.unboxnow.product.dao.BrandRepo;
import com.unboxnow.product.dao.CategoryRepo;
import com.unboxnow.product.dao.DiscountRepo;
import com.unboxnow.product.dao.ProductRepo;
import com.unboxnow.product.dto.ProductDTO;
import com.unboxnow.product.entity.Brand;
import com.unboxnow.product.entity.Category;
import com.unboxnow.product.entity.Discount;
import com.unboxnow.product.entity.Product;
import com.unboxnow.product.messaging.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@EnableCaching
@CacheConfig(cacheNames = "product/products")
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;

    private final CategoryRepo categoryRepo;

    private final BrandRepo brandRepo;

    private final DiscountRepo discountRepo;

    private final Producer producer;

    @Autowired
    public ProductServiceImpl(ProductRepo productRepo,
                              CategoryRepo categoryRepo,
                              BrandRepo brandRepo,
                              DiscountRepo discountRepo,
                              Producer producer) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
        this.brandRepo = brandRepo;
        this.discountRepo = discountRepo;
        this.producer = producer;
    }

    @Override
    public List<ProductDTO> findAll() {
        List<Product> products = productRepo.findAll();
        return ProductDTO.fromEntities(products);
    }

    @Override
    @Cacheable(key = "#theId")
    public ProductDTO findById(int theId) {
        Product product = productRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Product", theId));
        return ProductDTO.fromEntity(product);
    }

    @Override
    public List<ProductDTO> findByCategoryId(int theId) {
        List<Product> products = productRepo.findByCategoryId(theId);
        return ProductDTO.fromEntities(products);
    }

    @Override
    public List<ProductDTO> findByBrandId(int theId) {
        List<Product> products = productRepo.findByBrandId(theId);
        return ProductDTO.fromEntities(products);
    }

    @Override
    public List<ProductDTO> findByDiscountId(int theId) {
        List<Product> products = productRepo.findByDiscountId(theId);
        return ProductDTO.fromEntities(products);
    }

    @Override
    public List<ProductDTO> findBySku(String sku) {
        List<Product> products = productRepo.findBySku(sku);
        return ProductDTO.fromEntities(products);
    }

    @Override
    @CachePut(key = "#result.id")
    public ProductDTO create(ProductDTO dto) {
        Category category = null;
        int categoryId = dto.getCategoryId();
        if (categoryId > 0) {
            category = categoryRepo.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Category", categoryId));
        }
        Brand brand = null;
        int brandId = dto.getBrandId();
        if (brandId > 0) {
            brand = brandRepo.findById(brandId)
                    .orElseThrow(() -> new NotFoundException("Brand", brandId));
        }
        Discount discount = null;
        int discountId = dto.getDiscountId();
        if (discountId > 0) {
            discount = discountRepo.findById(discountId)
                    .orElseThrow(() -> new NotFoundException("Discount", discountId));
        }
        Product product = Product.fromDTO(dto);
        product.setId(0);
        product.setCategory(null);
        product.setBrand(null);
        product.setDiscount(null);
        Product dbProduct = productRepo.save(product);
        dbProduct.setCategory(category);
        dbProduct.setBrand(brand);
        dbProduct.setDiscount(discount);
        return ProductDTO.fromEntity(productRepo.save(dbProduct));
    }

    @Override
    @CachePut(key = "#result.id")
    public ProductDTO update(ProductDTO dto) {
        int theId = dto.getId();
        Product dbProduct = productRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Product", theId));
        Product product = Product.fromDTO(dto);
        // update category
        int categoryId = dto.getCategoryId();
        if (categoryId == 0) {
            product.setCategory(null);
        } else if (dbProduct.getCategory() == null || dbProduct.getCategory().getId() != categoryId) {
            Category newCategory = categoryRepo.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Category", categoryId));
            product.setCategory(newCategory);
        }
        // update brand
        int brandId = dto.getBrandId();
        if (brandId == 0) {
            product.setBrand(null);
        } else if (dbProduct.getBrand() == null || dbProduct.getBrand().getId() != brandId) {
            Brand newBrand = brandRepo.findById(brandId)
                    .orElseThrow(() -> new NotFoundException("Brand", brandId));
            product.setBrand(newBrand);
        }
        // update discount
        int discountId = dto.getDiscountId();
        if (discountId == 0) {
            product.setDiscount(null);
        } else if (dbProduct.getDiscount() == null || dbProduct.getDiscount().getId() != discountId) {
            Discount newDiscount = discountRepo.findById(discountId)
                    .orElseThrow(() -> new NotFoundException("Discount", discountId));
            product.setDiscount(newDiscount);
        }
        ProductDTO res = ProductDTO.fromEntity(productRepo.save(product));
        publishIfUpdated(ProductDTO.fromEntity(dbProduct), res);
        return res;
    }

    @Override
    @CachePut(key = "#result.id")
    public ProductDTO partiallyUpdate(int theId, ProductDTO dto) {
        Product dbProduct = this.productRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Product", theId));

        ProductDTO prev = ProductDTO.fromEntity(dbProduct);

        String updateField = dto.getTitle();
        if (updateField != null) dbProduct.setTitle(updateField);

        updateField = dto.getDescription();
        if (updateField != null) dbProduct.setDescription(updateField);

        updateField = dto.getColor();
        if (updateField != null) dbProduct.setColor(updateField);

        updateField = dto.getSize();
        if (updateField != null) dbProduct.setSize(updateField);

        updateField = dto.getSku();
        if (updateField != null) dbProduct.setSku(updateField);

        BigDecimal updatePrice = dto.getPrice();
        if (updatePrice != null) dbProduct.setPrice(updatePrice);

        // update category
        int categoryId = dto.getCategoryId();
        if (categoryId != 0) {
            if (categoryId == -1) {
                dbProduct.setCategory(null);
            } else if (dbProduct.getCategory() == null || dbProduct.getCategory().getId() != categoryId) {
                Category newCategory = categoryRepo.findById(categoryId)
                        .orElseThrow(() -> new NotFoundException("Category", categoryId));
                dbProduct.setCategory(newCategory);
            }
        }

        // update brand
        int brandId = dto.getBrandId();
        if (brandId != 0) {
            if (brandId == -1) {
                dbProduct.setBrand(null);
            } else if (dbProduct.getBrand() == null || dbProduct.getBrand().getId() != brandId) {
                Brand newBrand = brandRepo.findById(brandId)
                        .orElseThrow(() -> new NotFoundException("Brand", brandId));
                dbProduct.setBrand(newBrand);
            }
        }

        // update discount
        int discountId = dto.getDiscountId();
        if (discountId != 0) {
            if (discountId == -1) {
                dbProduct.setDiscount(null);
            } else if (dbProduct.getDiscount() == null || dbProduct.getDiscount().getId() != discountId) {
                Discount newDiscount = discountRepo.findById(discountId)
                        .orElseThrow(() -> new NotFoundException("Discount", discountId));
                dbProduct.setDiscount(newDiscount);
            }
        }

        ProductDTO res = ProductDTO.fromEntity(productRepo.save(dbProduct));
        publishIfUpdated(prev, res);
        return res;
    }

    @Override
    @CacheEvict(key = "#theId")
    public void deleteById(int theId) {
        Optional<Product> optional = this.productRepo.findById(theId);
        if (optional.isEmpty()) {
            throw new NotFoundException("Product", theId);
        }
        productRepo.deleteById(theId);
        producer.publish(new Message(theId), Topic.UPDATE_PRODUCT);
    }

    private void publishIfUpdated(ProductDTO prev, ProductDTO curr) {
        boolean updated = !prev.getPrice().equals(curr.getPrice());
        if (!updated) updated = !prev.getSku().equals(curr.getSku());
        if (!updated) {
            if (curr.getDiscountId() > 0 || prev.getDiscountId() > 0) {
                updated = prev.getDiscountId() != curr.getDiscountId();
            }
        }
        if (!updated) updated = prev.generateAssembledTitle().equals(curr.generateAssembledTitle());
        if (updated) {
            producer.publish(new Message(curr.getId()), Topic.UPDATE_PRODUCT);
        }
    }
}
