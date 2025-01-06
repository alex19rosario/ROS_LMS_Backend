package com.ros.ports_outbound.dao;

import com.ros.entities.Author;

import java.util.Optional;

public interface AuthorDAO {
    Optional<Author> findByFullName(String firstName, String middleName, String lastName);
    Optional<Author> findByFirstAndLastName(String firstName, String lastName);
    Optional<Author> findByFirstAndMiddleAndLastName(String firstName, String middleName, String lastName);
}
