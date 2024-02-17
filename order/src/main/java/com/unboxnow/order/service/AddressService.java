package com.unboxnow.order.service;

import com.unboxnow.order.dto.AddressDTO;

import java.util.List;

public interface AddressService {

    List<AddressDTO> findAll();

    AddressDTO findById(int theId);

    AddressDTO findByOrderId(int theId);

}
