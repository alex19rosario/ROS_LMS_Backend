package com.ros.lms.ports.outbound.repository_contracts;

import com.ros.lms.domain.entities.Member;
import com.ros.lms.domain.enums.MemberStatuses;

import java.util.Optional;

public interface MemberDAO {
    void create(Member member);
    Optional<Member> findByGovernmentID(String governmentID);
    Optional<Member> findByUsername(String username);
    Optional<Member> findByEmail(String email);
}
