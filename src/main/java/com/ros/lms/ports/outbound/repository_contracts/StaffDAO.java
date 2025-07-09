package com.ros.lms.ports.outbound.repository_contracts;

import com.ros.lms.domain.entities.Staff;

import java.util.Optional;

public interface StaffDAO {
    Optional<Staff> findByUsername(String username);
}
