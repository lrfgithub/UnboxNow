package com.unboxnow.product.entity;

import com.unboxnow.product.dto.DiscountDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "discount")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    @NotBlank
    private String title;

    @Column(name = "description")
    @Lob
    private String description;

    @Column(name = "percent", columnDefinition = "DECIMAL(3,2)")
    @NotNull
    @DecimalMin(value = "0", message = "Discount percent must be greater or equal than 0.")
    @DecimalMax(value = "1", message = "Discount percent must be less or equal than 1.")
    private BigDecimal percent;

    @Column(name = "active", columnDefinition = "TINYINT")
    @NotNull
    private Boolean active;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "discount",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Product> products;

    public Discount() {
    }

    public Discount(String title, String description, BigDecimal percent, Boolean active) {
        this.title = title;
        this.description = description;
        this.percent = percent == null? null : percent.setScale(2, RoundingMode.CEILING);
        this.active = active;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPercent() {
        return this.percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent == null? null : percent.setScale(2, RoundingMode.CEILING);
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    public List<Product> getProducts() {
        return this.products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Discount{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", percent=" + percent +
                ", active=" + active +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }

    public static Discount fromDTO(DiscountDTO dto) {
        if (dto == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Discount.class);
    }
}
