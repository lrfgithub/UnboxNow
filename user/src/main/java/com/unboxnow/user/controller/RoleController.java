package com.unboxnow.user.controller;

import com.unboxnow.user.dto.RoleDTO;
import com.unboxnow.user.service.RoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@Validated
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<RoleDTO> findAll() {
        return roleService.findAll();
    }

    @GetMapping("/{theId}")
    public RoleDTO findById(@Min(1) @PathVariable int theId) {
        return roleService.findById(theId);
    }

    @GetMapping("/title/{title}")
    public RoleDTO findByTitle(@NotBlank @PathVariable String title) {
        return roleService.findByTitle(title);
    }

    @PostMapping
    public RoleDTO create(@Valid @RequestBody RoleDTO dto) {
        return roleService.create(dto);
    }

    @PutMapping
    public RoleDTO update(@Valid @RequestBody RoleDTO dto) {
        return roleService.update(dto);
    }

    @PatchMapping("/{theId}")
    public RoleDTO partiallyUpdate(@Min(1) @PathVariable int theId, @RequestBody RoleDTO dto) {
        return roleService.partiallyUpdate(theId, dto);
    }

    @DeleteMapping("/{theId}")
    public String deleteById(@Min(1) @PathVariable int theId) {
        roleService.deleteById(theId);
        return "Role is deleted - " + theId;
    }
}
