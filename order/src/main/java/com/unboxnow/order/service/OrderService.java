package com.unboxnow.order.service;

import com.unboxnow.order.dto.MemberAddressDTO;
import com.unboxnow.order.dto.OrderDTO;

import java.util.List;

public interface OrderService {

    List<OrderDTO> findAll();

    OrderDTO findById(int theId);

    OrderDTO findByAddressId(int theId);

    List<OrderDTO> findByMemberId(int theId);

    OrderDTO submitAddress(MemberAddressDTO dto);

    void submitPayment(int theId);

    OrderDTO create(OrderDTO dto);

    void updateStatus(int theId);

    OrderDTO cancelById(int theId);

    void deleteById(int theId);

}
