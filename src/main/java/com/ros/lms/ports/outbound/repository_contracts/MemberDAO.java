package com.ros.lms.ports.outbound.repository_contracts;

import com.ros.lms.domain.entities.Member;
import com.ros.lms.domain.enums.MemberValidationStatus;

import java.util.Optional;
import java.util.Set;

public interface MemberDAO {
    void create(Member member);
    Optional<Member> findByGovernmentID(String governmentID);
    Optional<Member> findByUsername(String username);
    Optional<Member> findByEmail(String email);
    Set<MemberValidationStatus> validateMemberUniqueness(String governmentID, String username, String email);
}
