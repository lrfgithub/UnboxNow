package com.unboxnow.user.dto;

import jakarta.validation.constraints.NotBlank;

public class PasswordForm {

    private int memberId;

    private String oldPassword;

    @NotBlank
    private String newPassword;

    public PasswordForm() {
    }

    public PasswordForm(int memberId, String oldPassword, String newPassword) {
        this.memberId = memberId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "PasswordForm{" +
                "memberId=" + memberId +
                ", oldPassword='" + oldPassword + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
