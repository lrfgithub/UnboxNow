package com.unboxnow.user.service;

import com.unboxnow.user.dto.LoginForm;
import com.unboxnow.user.dto.MemberDTO;
import com.unboxnow.user.dto.PasswordForm;

import java.util.List;
import java.util.Map;

public interface MemberService {

    List<MemberDTO> findAll();

    MemberDTO findById(int theId);

    MemberDTO findByUsername(String username);

    Map<String, String> login(LoginForm form);

    Map<String, String> register(MemberDTO dto);

    MemberDTO updateInfo(MemberDTO dto);

    Map<String, String> updatePassword(PasswordForm form);

    Map<String, String> forgetPassword(String username);

    void resetPassword(PasswordForm form, String resetToken);

    void deactivateById(int theId);

    void deleteById(int theId);
}
