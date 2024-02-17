package com.unboxnow.product.dto;

import com.unboxnow.product.entity.Discount;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DiscountDTO{

    private int id;

    @NotBlank
    private String title;

    private String description;

    @NotNull
    @DecimalMin(value = "0", message = "Discount must be greater or equal than 0.")
    @DecimalMax(value = "1", message = "Discount must be less or equal than 1.")
    private BigDecimal percent;

    @NotNull
    private Boolean active;

    public DiscountDTO() {
    }

    public DiscountDTO(String title, String description, BigDecimal percent, Boolean active) {
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

    @Override
    public String toString() {
        return "DiscountDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", percent=" + percent +
                ", active=" + active +
                '}';
    }

    public static DiscountDTO fromEntity(Discount discount) {
        if (discount == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(discount, DiscountDTO.class);
    }
}
