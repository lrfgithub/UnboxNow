package com.unboxnow.user.service;

import com.unboxnow.common.exception.NotFoundException;
import com.unboxnow.user.dao.MemberRepo;
import com.unboxnow.user.dao.MemberRoleRepo;
import com.unboxnow.user.dao.RoleRepo;
import com.unboxnow.user.dto.MemberRoleDTO;
import com.unboxnow.user.entity.Member;
import com.unboxnow.user.entity.MemberRole;
import com.unboxnow.user.entity.MemberRoleId;
import com.unboxnow.user.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberRoleServiceImpl implements MemberRoleService {

    private final MemberRoleRepo memberRoleRepo;

    private final MemberRepo memberRepo;

    private final RoleRepo roleRepo;

    @Autowired
    public MemberRoleServiceImpl(MemberRoleRepo memberRoleRepo, MemberRepo memberRepo, RoleRepo roleRepo) {
        this.memberRoleRepo = memberRoleRepo;
        this.memberRepo = memberRepo;
        this.roleRepo = roleRepo;
    }

    @Override
    public List<MemberRoleDTO> findAll() {
        List<MemberRole> memberRoles = memberRoleRepo.findAll();
        return MemberRoleDTO.fromEntities(memberRoles);
    }

    @Override
    public MemberRoleDTO findById(MemberRoleId memberRoleId) {
        MemberRole memberRole = memberRoleRepo.findById(memberRoleId)
                .orElseThrow(() -> new NotFoundException("MemberRole", "memberRoleId", memberRoleId.toString()));
        return MemberRoleDTO.fromEntity(memberRole);
    }

    @Override
    public List<MemberRoleDTO> findByMemberId(int memberId) {
        List<MemberRole> memberRoles = memberRoleRepo.findByMemberId(memberId);
        return MemberRoleDTO.fromEntities(memberRoles);
    }

    @Override
    public List<MemberRoleDTO> findByRoleId(int roleId) {
        List<MemberRole> memberRoles = memberRoleRepo.findByRoleId(roleId);
        return MemberRoleDTO.fromEntities(memberRoles);
    }

    @Override
    public MemberRoleDTO create(MemberRoleDTO dto) {
        int memberId = dto.getMemberId(), roleId = dto.getRoleId();
        Optional<MemberRole> res = memberRoleRepo.findById(new MemberRoleId(memberId, roleId));
        if (res.isPresent()) {
            return MemberRoleDTO.fromEntity(res.get());
        }
        Member dbMember = memberRepo.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member", memberId));
        Role dbRole = roleRepo.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Role", roleId));
        MemberRole memberRole = new MemberRole(memberId, roleId);
        memberRole.setMember(dbMember);
        memberRole.setRole(dbRole);
        return MemberRoleDTO.fromEntity(memberRoleRepo.save(memberRole));
    }

    @Override
    public String deleteById(MemberRoleId memberRoleId) {
        MemberRole memberRole = memberRoleRepo.findById(memberRoleId)
                .orElseThrow(() ->  new NotFoundException("MemberRole", "memberRoleId", memberRoleId.toString()));
        String res = String.format("MemberRole is deleted - %s: %s",
                memberRole.getMember().getUsername(),
                memberRole.getRole().getTitle());
        memberRoleRepo.deleteById(memberRoleId);
        return res;
    }
}
