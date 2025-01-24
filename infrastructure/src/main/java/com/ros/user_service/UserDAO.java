package com.ros.user_service;

import com.ros.entities.User;
import com.ros.exceptions.UsernameAlreadyExistsException;

import java.util.Optional;

public interface UserDAO {
    void create(User user);
    Optional<User> findByUsername(String username);
}
