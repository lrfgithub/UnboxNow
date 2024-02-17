package com.unboxnow.user.dao;

import com.unboxnow.user.entity.MemberRole;
import com.unboxnow.user.entity.MemberRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRoleRepo extends JpaRepository<MemberRole, MemberRoleId> {

    List<MemberRole> findByMemberId(int memberId);

    List<MemberRole> findByRoleId(int roleId);

}
