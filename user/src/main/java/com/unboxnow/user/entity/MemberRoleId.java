package com.unboxnow.user.entity;

import jakarta.validation.constraints.Min;

import java.io.Serializable;
import java.util.Objects;

public class MemberRoleId implements Serializable {

    @Min(1)
    private int memberId;

    @Min(1)
    private int roleId;

    public MemberRoleId() {
    }

    public MemberRoleId(int memberId, int roleId) {
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

    @Override
    public String toString() {
        return "MemberRoleId{" +
                "memberId=" + memberId +
                ", roleId=" + roleId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberRoleId that = (MemberRoleId) o;
        return memberId == that.memberId && roleId == that.roleId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, roleId);
    }
}
