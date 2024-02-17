package com.unboxnow.user.dao;

import com.unboxnow.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepo extends JpaRepository<Member, Integer> {

    Optional<Member> findByUsername(String username);
}
