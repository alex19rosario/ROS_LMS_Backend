package com.ros.ports_outbound.dao;

import com.ros.entities.Member;

import java.util.Optional;

public interface MemberDAO {
    void create(Member member);
    Optional<Member> findByGovernmentID(String governmentID);
    Optional<Member> findByUsername(String username);
    Optional<Member> findByEmail(String email);

}
