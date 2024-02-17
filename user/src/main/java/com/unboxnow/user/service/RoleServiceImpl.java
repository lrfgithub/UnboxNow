package com.unboxnow.user.service;

import com.unboxnow.common.exception.ApplicableException;
import com.unboxnow.common.exception.DuplicateException;
import com.unboxnow.common.exception.NotFoundException;
import com.unboxnow.user.dao.MemberRoleRepo;
import com.unboxnow.user.dao.RoleRepo;
import com.unboxnow.user.dto.RoleDTO;
import com.unboxnow.user.entity.MemberRole;
import com.unboxnow.user.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleRepo;

    private final MemberRoleRepo memberRoleRepo;

    @Autowired
    public RoleServiceImpl(RoleRepo roleRepo, MemberRoleRepo memberRoleRepo) {
        this.roleRepo = roleRepo;
        this.memberRoleRepo = memberRoleRepo;
    }

    @Override
    public List<RoleDTO> findAll() {
        List<Role> roles = roleRepo.findAll();
        return RoleDTO.fromEntities(roles);
    }

    @Override
    public RoleDTO findById(int theId) {
        Role role = roleRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Role", theId));
        return RoleDTO.fromEntity(role);
    }

    @Override
    public RoleDTO findByTitle(String title) {
        Role role = roleRepo.findByTitle(title)
                .orElseThrow(() -> new NotFoundException("Role", "title", title));
        return RoleDTO.fromEntity(role);
    }

    @Override
    public RoleDTO create(RoleDTO dto) {
        Optional<Role> opt = roleRepo.findByTitle(dto.getTitle());
        if (opt.isPresent()) {
            throw new DuplicateException("Role", "title", dto.getTitle());
        }
        Role role = Role.fromDTO(dto);
        role.setId(0);
        return RoleDTO.fromEntity(roleRepo.save(role));
    }

    @Override
    public RoleDTO update(RoleDTO dto) {
        int theId = dto.getId();
        Role dbRoleById = roleRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Role", theId));
        if (!dbRoleById.getTitle().equals(dto.getTitle())) {
            Optional<Role> opt = roleRepo.findByTitle(dto.getTitle());
            if (opt.isPresent()) {
                throw new DuplicateException("Role", "title", dto.getTitle());
            }
        }
        Role role = Role.fromDTO(dto);
        return RoleDTO.fromEntity(roleRepo.save(role));
    }

    @Override
    public RoleDTO partiallyUpdate(int theId, RoleDTO dto) {
        Role dbRole = roleRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Role", theId));

        String updateField = dto.getTitle();
        if (updateField != null) {
            Optional<Role> opt = roleRepo.findByTitle(updateField);
            if (opt.isPresent()) {
                throw new DuplicateException("Role", "title", updateField);
            }
            dbRole.setTitle(updateField);
        }

        updateField = dto.getDescription();
        if (updateField != null) dbRole.setDescription(updateField);

        return RoleDTO.fromEntity(roleRepo.save(dbRole));
    }

    @Override
    public void deleteById(int theId) {
        Optional<Role> res = roleRepo.findById(theId);
        if (res.isEmpty()) {
            throw new NotFoundException("Role", theId);
        }
        List<MemberRole> memberRoles = memberRoleRepo.findByRoleId(theId);
        if (!memberRoles.isEmpty()) {
            throw new ApplicableException("Role", theId, "MemberRole");
        }
        roleRepo.deleteById(theId);
    }
}
