package com.unboxnow.user.service;

import com.unboxnow.user.dto.AddressDTO;

import java.util.List;

public interface AddressService {

    List<AddressDTO> findAll();

    AddressDTO findById(int theId);

    List<AddressDTO> findByMemberId(int theId);

    AddressDTO create(AddressDTO dto);

    AddressDTO update(AddressDTO dto);

    void deleteById(int theId);

}
