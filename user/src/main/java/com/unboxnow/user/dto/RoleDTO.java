package com.unboxnow.user.dto;

import com.unboxnow.user.entity.Role;
import jakarta.validation.constraints.NotBlank;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoleDTO {

    private int id;

    @NotBlank
    private String title;

    private String description;

    public RoleDTO() {
    }

    public RoleDTO(String title, String description) {
        this.title = title;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "RoleDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public static RoleDTO fromEntity(Role role) {
        if (role == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(role, RoleDTO.class);
    }

    public static List<RoleDTO> fromEntities(List<Role> roles) {
        if (roles.isEmpty()) return new ArrayList<>();
        return roles.stream()
                .map(RoleDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
