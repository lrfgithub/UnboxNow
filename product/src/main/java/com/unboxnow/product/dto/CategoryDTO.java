package com.unboxnow.product.dto;

import com.unboxnow.product.entity.Category;
import jakarta.validation.constraints.NotBlank;
import org.modelmapper.ModelMapper;

public class CategoryDTO {

    private int id;

    @NotBlank
    private String title;

    private String description;

    public CategoryDTO() {
    }

    public CategoryDTO(String title, String description) {
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
        return "CategoryDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static CategoryDTO fromEntity(Category category) {
        if (category == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(category, CategoryDTO.class);
    }
}
