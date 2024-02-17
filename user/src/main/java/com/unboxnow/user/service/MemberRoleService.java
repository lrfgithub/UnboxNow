package com.unboxnow.user.service;

import com.unboxnow.user.dto.MemberRoleDTO;
import com.unboxnow.user.entity.MemberRoleId;

import java.util.List;

public interface MemberRoleService {

    List<MemberRoleDTO> findAll();

    MemberRoleDTO findById(MemberRoleId memberRoleId);

    List<MemberRoleDTO> findByMemberId(int memberId);

    List<MemberRoleDTO> findByRoleId(int roleId);

    MemberRoleDTO create(MemberRoleDTO dto);

    String deleteById(MemberRoleId memberRoleId);
}
