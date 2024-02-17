package com.unboxnow.user.controller;

import com.unboxnow.user.dto.LoginForm;
import com.unboxnow.user.dto.MemberDTO;
import com.unboxnow.user.dto.PasswordForm;
import com.unboxnow.user.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/members")
@Validated
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public List<MemberDTO> findAll() {
        return memberService.findAll();
    }

    @GetMapping("/{theId}")
    public MemberDTO findById(@Min(1) @PathVariable int theId) {
        return memberService.findById(theId);
    }

    @GetMapping("/username/{username}")
    public MemberDTO findByUsername(@NotBlank @PathVariable String username) {
        return memberService.findByUsername(username);
    }

    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody LoginForm form) {
        return memberService.login(form);
    }

    @PostMapping("/register")
    public Map<String, String> register(@Valid @RequestBody MemberDTO dto) {
        return memberService.register(dto);
    }

    @PutMapping
    public MemberDTO updateInfo(@Valid @RequestBody MemberDTO dto) {
        return memberService.updateInfo(dto);
    }

    @PostMapping("/update-password")
    public Map<String, String> updatePassword(@Valid @RequestBody PasswordForm form) {
        return memberService.updatePassword(form);
    }

    @PostMapping("/forget-password/{username}")
    public Map<String, String> forgetPassword(@NotBlank @PathVariable String username) {
        return memberService.forgetPassword(username);
    }

    @PostMapping("/reset-password/{resetToken}")
    public void resetPassword(@Valid @RequestBody PasswordForm form, @NotBlank @PathVariable String resetToken) {
        memberService.resetPassword(form, resetToken);
    }

    @PatchMapping("/{theId}")
    public void deactivateById(@Min(1) @PathVariable int theId) {
        memberService.deactivateById(theId);
    }

    @DeleteMapping("/{theId}")
    public String deleteById(@Min(1) @PathVariable int theId) {
        memberService.deleteById(theId);
        return "Member is deleted - " + theId;
    }
}
