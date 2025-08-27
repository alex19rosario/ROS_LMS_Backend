package com.ros.lms.ports.outbound.repository_contracts;

import com.ros.lms.domain.entities.AuthorityType;
import com.ros.lms.domain.enums.RoleType;

import java.util.Optional;

public interface AuthorityTypeDAO {
    Optional<AuthorityType> findByLabel(RoleType label);
}
