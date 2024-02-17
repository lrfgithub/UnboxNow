package com.unboxnow.user.dto;

import com.unboxnow.user.entity.MemberRole;
import jakarta.validation.constraints.Min;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MemberRoleDTO {

    @Min(1)
    private int memberId;

    private String username;

    @Min(1)
    private int roleId;

    private String roleTitle;

    public MemberRoleDTO() {
    }

    public MemberRoleDTO(int memberId, int roleId) {
        this.memberId = memberId;
        this.roleId = roleId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }

    @Override
    public String toString() {
        return "MemberRoleDTO{" +
                "memberId=" + memberId +
                ", username='" + username + '\'' +
                ", roleId=" + roleId +
                ", roleTitle='" + roleTitle + '\'' +
                '}';
    }

    public static MemberRoleDTO fromEntity(MemberRole memberRole) {
        if (memberRole == null) return null;
        ModelMapper modelMapper = new ModelMapper();
        PropertyMap<MemberRole, MemberRoleDTO> memberRoleMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setMemberId(source.getMemberId());
                map().setUsername(source.getMember().getUsername());
                map().setRoleId(source.getRoleId());
                map().setRoleTitle(source.getRole().getTitle());
            }
        };
        modelMapper.addMappings(memberRoleMap);
        return modelMapper.map(memberRole, MemberRoleDTO.class);
    }

    public static List<MemberRoleDTO> fromEntities(List<MemberRole> memberRoles) {
        if (memberRoles.isEmpty()) return new ArrayList<>();
        return memberRoles.stream()
                .map(MemberRoleDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
