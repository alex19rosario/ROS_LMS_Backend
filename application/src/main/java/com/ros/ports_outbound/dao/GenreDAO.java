package com.ros.ports_outbound.dao;

import com.ros.entities.Genre;

import java.util.Optional;

public interface GenreDAO {
    Optional<Genre> findByDescription(String description);
}
