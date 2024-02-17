package com.unboxnow.user.service;

import com.unboxnow.common.exception.NotFoundException;
import com.unboxnow.user.dao.AddressRepo;
import com.unboxnow.user.dao.MemberRepo;
import com.unboxnow.user.dto.AddressDTO;
import com.unboxnow.user.entity.Address;
import com.unboxnow.user.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepo addressRepo;

    private final MemberRepo memberRepo;

    @Autowired
    public AddressServiceImpl(AddressRepo addressRepo, MemberRepo memberRepo) {
        this.addressRepo = addressRepo;
        this.memberRepo = memberRepo;
    }

    @Override
    public List<AddressDTO> findAll() {
        return AddressDTO.fromEntities(addressRepo.findAll());
    }

    @Override
    public AddressDTO findById(int theId) {
        Address address = addressRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Address", theId));
        return AddressDTO.fromEntity(address);
    }

    @Override
    public List<AddressDTO> findByMemberId(int theId) {
        return AddressDTO.fromEntities(addressRepo.findByMemberId(theId));
    }

    @Override
    public AddressDTO create(AddressDTO dto) {
        int memberId = dto.getMemberId();
        if (memberId == 0) {
            throw new NotFoundException("Member", 0);
        }
        Member member = memberRepo.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member", memberId));
        Address address = Address.fromDTO(dto);
        address.setId(0);
        address.setMember(null);
        Address dbAddress = addressRepo.save(address);
        dbAddress.setMember(member);
        return AddressDTO.fromEntity(addressRepo.save(dbAddress));
    }

    @Override
    public AddressDTO update(AddressDTO dto) {
        int theId = dto.getId();
        Address dbAddress = addressRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Address", theId));
        Address address = Address.fromDTO(dto);
        int memberId = dto.getMemberId();
        if (memberId == 0) {
            throw new NotFoundException("Member", memberId);
        } else {
            Member member;
            if (memberId == dbAddress.getId()) {
                member = dbAddress.getMember();
            } else {
                member = memberRepo.findById(memberId)
                        .orElseThrow(() -> new NotFoundException("Member", memberId));
            }
            address.setMember(member);
        }
        return AddressDTO.fromEntity(addressRepo.save(address));
    }

    @Override
    public void deleteById(int theId) {
        Optional<Address> opt = addressRepo.findById(theId);
        if (opt.isEmpty()) {
            throw new NotFoundException("Address", theId);
        }
        addressRepo.deleteById(theId);
    }
}
