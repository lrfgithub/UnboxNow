package com.unboxnow.user.service;

import com.unboxnow.user.dto.RoleDTO;

import java.util.List;

public interface RoleService {

    List<RoleDTO> findAll();

    RoleDTO findById(int theId);

    RoleDTO findByTitle(String title);

    RoleDTO create(RoleDTO dto);

    RoleDTO update(RoleDTO dto);

    RoleDTO partiallyUpdate(int theId, RoleDTO dto);

    void deleteById(int theId);
}
