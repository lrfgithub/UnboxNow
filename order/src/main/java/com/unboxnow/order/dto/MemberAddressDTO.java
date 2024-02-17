package com.unboxnow.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class MemberAddressDTO {

    @NotNull
    @Min(1)
    private Integer memberId;

    @NotNull
    @Min(1)
    private Integer AddressId;

    public MemberAddressDTO() {
    }

    public MemberAddressDTO(Integer memberId, Integer addressId) {
        this.memberId = memberId;
        AddressId = addressId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getAddressId() {
        return AddressId;
    }

    public void setAddressId(Integer addressId) {
        AddressId = addressId;
    }

    @Override
    public String toString() {
        return "MemberAddressDTO{" +
                "memberId=" + memberId +
                ", AddressId=" + AddressId +
                '}';
    }
}
