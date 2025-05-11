package com.ros.lms.ports.outbound.repository_contracts;


import com.ros.lms.domain.entities.User;

import java.util.Optional;

public interface UserDAO {
    void create(User user);
    Optional<User> findByUsername(String username);
}
