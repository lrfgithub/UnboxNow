package com.unboxnow.product.dto;

import com.unboxnow.product.entity.Brand;
import jakarta.validation.constraints.NotBlank;
import org.modelmapper.ModelMapper;

public class BrandDTO {

    private int id;

    @NotBlank
    private String title;

    private String description;

    public BrandDTO() {
    }

    public BrandDTO(String title, String description) {
        this.title = title;
        this.description = description;
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

    @Override
    public String toString() {
        return "BrandDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static BrandDTO fromEntity(Brand brand) {
        if (brand == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(brand, BrandDTO.class);
    }
}
