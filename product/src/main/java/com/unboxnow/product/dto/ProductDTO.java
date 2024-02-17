package com.unboxnow.product.dto;

import com.unboxnow.product.entity.Product;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDTO implements Serializable {

    private int id;

    @NotBlank
    private String title;

    @Min(0)
    private int categoryId;

    private String categoryTitle;

    @Min(0)
    private int brandId;

    private String brandTitle;

    private String description;

    private String color;

    private String size;

    @NotBlank
    private String sku;

    @NotNull
    @DecimalMin(value = "0.0", message = "Product price must be greater or equal than 1")
    private BigDecimal price;

    @Min(0)
    private int discountId;

    private String discountTitle;

    @DecimalMin(value = "0", message = "Discount percent must be greater or equal than 0.")
    @DecimalMax(value = "1", message = "Discount percent must be less or equal than 1.")
    private BigDecimal discountPercent;

    private boolean discountActive;

    public ProductDTO() {
    }

    public ProductDTO(String title, String description, String color, String size, String sku, BigDecimal price) {
        this.title = title;
        this.description = description;
        this.color = color;
        this.size = size;
        this.sku = sku;
        this.price = price == null? null : price.setScale(2, RoundingMode.CEILING);
    }

    public ProductDTO(int id,
                      String title,
                      String description,
                      String color,
                      String size,
                      String sku,
                      BigDecimal price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.color = color;
        this.size = size;
        this.sku = sku;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandTitle() {
        return brandTitle;
    }

    public void setBrandTitle(String brandTitle) {
        this.brandTitle = brandTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return this.price == null? null : this.price.setScale(2, RoundingMode.CEILING);
    }

    public void setPrice(BigDecimal price) {
        this.price = price == null? null : price.setScale(2, RoundingMode.CEILING);
    }

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public String getDiscountTitle() {
        return discountTitle;
    }

    public void setDiscountTitle(String discountTitle) {
        this.discountTitle = discountTitle;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent =
                discountPercent == null? null : discountPercent.setScale(2, RoundingMode.CEILING);
    }

    public boolean getDiscountActive() {
        return discountActive;
    }

    public void setDiscountActive(boolean discountActive) {
        this.discountActive = discountActive;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", categoryId=" + categoryId +
                ", categoryTitle='" + categoryTitle + '\'' +
                ", brandId=" + brandId +
                ", brandTitle='" + brandTitle + '\'' +
                ", description='" + description + '\'' +
                ", color='" + color + '\'' +
                ", size='" + size + '\'' +
                ", sku='" + sku + '\'' +
                ", price=" + price +
                ", discountId=" + discountId +
                ", discountTitle='" + discountTitle + '\'' +
                ", discountPercent=" + discountPercent +
                ", discountActive=" + discountActive +
                '}';
    }

    public String generateAssembledTitle() {
        StringBuilder assembledTitle = new StringBuilder(title);
        if (color != null) {
            assembledTitle.append(" - ").append(color);
        }
        if (size != null) {
            assembledTitle.append(" - ").append(size);
        }
        return assembledTitle.toString();
    }

    public BigDecimal generateFinalPrice() {
        BigDecimal finalPrice = price;
        if (discountActive && discountPercent != null) {
            finalPrice = finalPrice.multiply(discountPercent);
        }
        return finalPrice.setScale(2, RoundingMode.CEILING);
    }

    public static ProductDTO fromEntity(Product product) {
        if (product == null) return null;
        ProductDTO dto = new ProductDTO(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getColor(),
                product.getSize(),
                product.getSku(),
                product.getPrice()
        );
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryTitle(product.getCategory().getTitle());
        }
        if (product.getBrand() != null) {
            dto.setBrandId(product.getBrand().getId());
            dto.setBrandTitle(product.getBrand().getTitle());
        }
        if (product.getDiscount() != null) {
            dto.setDiscountId(product.getDiscount().getId());
            dto.setDiscountTitle(product.getDiscount().getTitle());
            dto.setDiscountPercent(product.getDiscount().getPercent());
            dto.setDiscountActive(product.getDiscount().getActive());
        }
        return dto;
    }

    public static List<ProductDTO> fromEntities(List<Product> products) {
        if (products == null) return null;
        else if (products.isEmpty()) return new ArrayList<>();
        return products.stream()
                .map(ProductDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
