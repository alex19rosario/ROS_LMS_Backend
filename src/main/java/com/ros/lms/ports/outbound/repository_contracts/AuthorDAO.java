package com.ros.lms.ports.outbound.repository_contracts;

import com.ros.lms.domain.entities.Author;

import java.util.Optional;

public interface AuthorDAO {
    Optional<Author> findByFullName(String firstName, String middleName, String lastName);
    Optional<Author> findByFirstAndLastName(String firstName, String lastName);
    Optional<Author> findByFirstAndMiddleAndLastName(String firstName, String middleName, String lastName);
}
