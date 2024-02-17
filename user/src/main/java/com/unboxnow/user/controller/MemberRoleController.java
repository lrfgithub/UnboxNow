package com.unboxnow.user.controller;

import com.unboxnow.user.dto.MemberRoleDTO;
import com.unboxnow.user.entity.MemberRoleId;
import com.unboxnow.user.service.MemberRoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member-roles")
@Validated
public class MemberRoleController {

    private final MemberRoleService memberRoleService;

    @Autowired
    public MemberRoleController(MemberRoleService memberRoleService) {
        this.memberRoleService = memberRoleService;
    }

    @GetMapping
    public List<MemberRoleDTO> findAll() {
        return memberRoleService.findAll();
    }

    @GetMapping("/member-role-id")
    public MemberRoleDTO findById(@Valid @RequestBody MemberRoleId memberRoleId) {
        return memberRoleService.findById(memberRoleId);
    }

    @GetMapping("/member/{memberId}")
    public List<MemberRoleDTO> findByMemberId(@Min(1) @PathVariable int memberId) {
        return memberRoleService.findByMemberId(memberId);
    }

    @GetMapping("/role/{roleId}")
    public List<MemberRoleDTO> findByRoleId(@Min(1) @PathVariable int roleId) {
        return memberRoleService.findByRoleId(roleId);
    }

    @PostMapping
    public MemberRoleDTO create(@Valid @RequestBody MemberRoleDTO dto) {
        return memberRoleService.create(dto);
    }

    @DeleteMapping
    public String deleteById(@Valid @RequestBody MemberRoleId memberRoleId) {
        return memberRoleService.deleteById(memberRoleId);
    }
}
