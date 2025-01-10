package com.ros.ports_outbound.dao;

import com.ros.entities.Genre;

import java.util.Optional;
import java.util.Set;

public interface GenreDAO {
    Optional<Genre> findByDescription(String description);
    Set<String> findAll();
}
