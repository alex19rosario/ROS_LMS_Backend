package com.ros.lms.ports.outbound.repository_contracts;

import com.ros.lms.domain.entities.Genre;

import java.util.Optional;
import java.util.Set;

public interface GenreDAO {
    Optional<Genre> findByDescription(String description);
    Set<String> findAll();
}
