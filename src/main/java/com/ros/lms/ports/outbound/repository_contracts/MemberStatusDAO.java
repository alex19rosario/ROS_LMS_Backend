package com.ros.lms.ports.outbound.repository_contracts;

import com.ros.lms.domain.enums.MemberStatuses;

import java.util.Optional;

public interface MemberStatusDAO {
    Optional<MemberStatuses> findStatusByMemberId(long memberId);
}
