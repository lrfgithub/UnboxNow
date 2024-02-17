package com.unboxnow.user.entity;

import com.unboxnow.user.dto.MemberRoleDTO;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "member_role")
@IdClass(MemberRoleId.class)
public class MemberRole implements Serializable {

    @Id
    @Column(name = "member_id")
    @Min(1)
    private int memberId;

    @Id
    @Column(name = "role_id")
    @Min(1)
    private int roleId;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "member_id", referencedColumnName = "id", insertable = false)
    @Valid
    private Member member;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "role_id", referencedColumnName = "id", insertable = false)
    @Valid
    private Role role;

    public MemberRole() {
    }

    public MemberRole(int memberId, int roleId) {
        this.memberId = memberId;
        this.roleId = roleId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "MemberRole{" +
                "memberId=" + memberId +
                ", roleId=" + roleId +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }

    public static MemberRole fromDTO(MemberRoleDTO dto) {
        if (dto == null) {
            return null;
        }
        ModelMapper modelMapper = new ModelMapper();
        PropertyMap<MemberRoleDTO, MemberRole> memberRoleDTOMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setMemberId(source.getMemberId());
                map().setRoleId(source.getRoleId());
            }
        };
        modelMapper.addMappings(memberRoleDTOMap);
        return modelMapper.map(dto, MemberRole.class);
    }
}
