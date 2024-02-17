package com.unboxnow.product.entity;

import com.unboxnow.product.dto.ProductDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    @NotBlank
    private String title;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "category_id")
    @Valid
    private Category category;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "brand_id")
    @Valid
    private Brand brand;

    @Column(name = "description")
    @Lob
    private String description;

    @Column(name = "color")
    private String color;

    @Column(name = "size")
    private String size;

    @Column(name = "SKU")
    @NotBlank
    private String sku;

    @Column(name = "price", columnDefinition = "DECIMAL(10,2)")
    @NotNull
    @DecimalMin(value = "0.0", message = "product price must be greater or equal than 0.0")
    private BigDecimal price;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "discount_id")
    @Valid
    private Discount discount;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    public Product() {
    }

    public Product(int id, String title, String description, String color, String size, String sku, BigDecimal price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.color = color;
        this.size = size;
        this.sku = sku;
        this.price = price;
    }

    public Product(String title,
                   Category category,
                   Brand brand,
                   String description,
                   String color,
                   String size,
                   String sku,
                   BigDecimal price,
                   Discount discount) {
        this.title = title;
        this.category = category;
        this.brand = brand;
        this.description = description;
        this.color = color;
        this.size = size;
        this.sku = sku;
        this.price = price == null? null : price.setScale(2, RoundingMode.CEILING);
        this.discount = discount;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Brand getBrand() {
        return this.brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSku() {
        return this.sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price == null? null : price.setScale(2, RoundingMode.CEILING);
    }

    public Discount getDiscount() {
        return this.discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return this.modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category=" + category +
                ", brand=" + brand +
                ", description='" + description + '\'' +
                ", color='" + color + '\'' +
                ", size='" + size + '\'' +
                ", sku='" + sku + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }

    public static Product fromDTO(ProductDTO dto) {
        if (dto == null) return null;
        return new Product(
                dto.getId(),
                dto.getTitle(),
                dto.getDescription(),
                dto.getColor(),
                dto.getSize(),
                dto.getSku(),
                dto.getPrice()
        );
    }
}
