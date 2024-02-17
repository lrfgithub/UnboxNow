package com.unboxnow.user.service;

import com.unboxnow.common.component.JWTProvider;
import com.unboxnow.common.constant.Token;
import com.unboxnow.common.constant.Topic;
import com.unboxnow.common.exception.DuplicateException;
import com.unboxnow.common.exception.LoginException;
import com.unboxnow.common.exception.NotCompletedException;
import com.unboxnow.common.exception.NotFoundException;
import com.unboxnow.common.message.Message;
import com.unboxnow.user.dao.MemberRepo;
import com.unboxnow.user.dao.MemberRoleRepo;
import com.unboxnow.user.dao.RoleRepo;
import com.unboxnow.user.dto.LoginForm;
import com.unboxnow.user.dto.MemberDTO;
import com.unboxnow.user.dto.PasswordForm;
import com.unboxnow.user.entity.Member;
import com.unboxnow.user.entity.MemberRole;
import com.unboxnow.user.entity.Role;
import com.unboxnow.user.messaging.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@ComponentScan(basePackages = {"com.unboxnow.common.component"})
public class MemberServiceImpl implements MemberService {

    private final MemberRepo memberRepo;

    private final MemberRoleRepo memberRoleRepo;

    private final RoleRepo roleRepo;

    private final JWTProvider jwtProvider;

    private final Producer producer;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberServiceImpl(MemberRepo memberRepo,
                             MemberRoleRepo memberRoleRepo,
                             RoleRepo roleRepo,
                             JWTProvider jwtProvider,
                             Producer producer) {
        this.memberRepo = memberRepo;
        this.memberRoleRepo = memberRoleRepo;
        this.roleRepo = roleRepo;
        this.jwtProvider = jwtProvider;
        this.producer = producer;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public List<MemberDTO> findAll() {
        List<Member> members = memberRepo.findAll();
        if (members.isEmpty()) return new ArrayList<>();
        return members.stream()
                .map(MemberDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public MemberDTO findById(int theId) {
        Member member = memberRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Member", theId));
        return MemberDTO.fromEntity(member);
    }

    @Override
    public MemberDTO findByUsername(String username) {
        Member member = memberRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Member", "username", username));
        return MemberDTO.fromEntity(member);
    }

    @Override
    public Map<String, String> login(LoginForm form) {
        String username = form.getUsername();
        Member member = memberRepo.findByUsername(username)
                .orElseThrow(() -> new LoginException(username, "member not found"));
        if (!member.getActive()) {
            throw new LoginException(username, "member not active");
        }
        int theId = member.getId();
        if (!passwordEncoder.matches(form.getPassword(), member.getPassword())) {
            throw new LoginException(username, "invalid password");
        }
        List<MemberRole> memberRoles = memberRoleRepo.findByMemberId(theId);
        List<String> roles = new ArrayList<>();
        if (memberRoles.isEmpty()) {
            throw new LoginException(username, "roles not found");
        } else {
            memberRoles.forEach(memberRole -> roles.add(memberRole.getRole().getTitle()));
        }
        return getAccessAndRefreshTokens(theId, roles);
    }

    @Override
    public Map<String, String> register(MemberDTO dto) {
        Optional<Member> opt = memberRepo.findByUsername(dto.getUsername());
        if (opt.isPresent()) {
            throw new DuplicateException("Member", "username", dto.getUsername());
        }
        Member member = Member.fromDTO(dto);
        member.setId(0);
        member.setPassword(passwordEncoder.encode(dto.getPassword()));
        member.setActive(true);
        Member dbMember = memberRepo.save(member);
        int memberId = dbMember.getId();
        try {
            MemberRole memberRole = new MemberRole(memberId, 3);
            memberRole.setMember(dbMember);
            Role customer = roleRepo.findById(3)
                    .orElseThrow(() -> new NotFoundException("Role", 3));
            memberRole.setRole(customer);
            memberRoleRepo.save(memberRole);
            List<String> roles = new ArrayList<>();
            roles.add("customer");
            return getAccessAndRefreshTokens(memberId, roles);
        } catch (Exception ex) {
            memberRepo.deleteById(dbMember.getId());
            throw new NotCompletedException(dto.getUsername());
        }
    }

    @Override
    public MemberDTO updateInfo(MemberDTO dto) {
        int theId = dto.getId();
        Member dbMember = memberRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Member", theId));
        dbMember.setFirstName(dto.getFirstName());
        dbMember.setLastName(dto.getLastName());
        return MemberDTO.fromEntity(memberRepo.save(dbMember));
    }

    @Override
    public Map<String, String> updatePassword(PasswordForm form) {
        int theId = form.getMemberId();
        Member dbMember = memberRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Member", theId));
        if (!passwordEncoder.matches(form.getOldPassword(), dbMember.getPassword())) {
            throw new LoginException(theId, "invalid old password");
        }
        dbMember.setPassword(passwordEncoder.encode(form.getNewPassword()));
        memberRepo.save(dbMember);
        List<String> roles = jwtProvider.getRolesById(theId);
        return getAccessAndRefreshTokens(theId, roles);
    }

    @Override
    public Map<String, String> forgetPassword(String username) {
        Member member = memberRepo.findByUsername(username)
                .orElseThrow(() -> new LoginException(username, "member not found"));
        return getResetTokens(member.getId());
    }

    @Override
    public void resetPassword(PasswordForm form, String resetToken) {
        int theId = form.getMemberId();
        jwtProvider.verifyResetToken(resetToken, theId);
        Member dbMember = memberRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Member", theId));
        dbMember.setPassword(passwordEncoder.encode(form.getNewPassword()));
        memberRepo.save(dbMember);
        jwtProvider.clearAllTokens(theId);
    }

    @Override
    public void deactivateById(int theId) {
        Member dbMember = memberRepo.findById(theId)
                .orElseThrow(() -> new NotFoundException("Member", theId));
        dbMember.setActive(false);
        memberRepo.save(dbMember);
        producer.publish(new Message(theId), Topic.UPDATE_MEMBER);
        jwtProvider.clearAccessAndRefreshTokens(theId);
    }

    @Override
    public void deleteById(int theId) {
        Optional<Member> res = memberRepo.findById(theId);
        if (res.isEmpty()) {
            throw new NotFoundException("Member", theId);
        }
        memberRepo.deleteById(theId);
        producer.publish(new Message(theId), Topic.UPDATE_MEMBER);
        jwtProvider.clearAccessAndRefreshTokens(theId);
    }

    private Map<String, String> getAccessAndRefreshTokens(int memberId, List<String> roles) {
        Map<String, String> tokens = new HashMap<>();
        String accessToken = jwtProvider.generateAccessToken(memberId, roles);
        tokens.put(Token.ACCESS.getHeaderKey(), accessToken);
        String refreshToken = jwtProvider.generateRefreshToken(memberId);
        tokens.put(Token.REFRESH.getHeaderKey(), refreshToken);
        return tokens;
    }

    private Map<String, String> getResetTokens(int memberId) {
        Map<String, String> tokens = new HashMap<>();
        String resetToken = jwtProvider.generateResetToken(memberId);
        tokens.put(Token.RESET.getHeaderKey(), resetToken);
        return tokens;
    }
}
