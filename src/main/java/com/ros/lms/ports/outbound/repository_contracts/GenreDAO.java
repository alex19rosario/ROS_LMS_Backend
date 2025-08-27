package com.ros.lms.ports.outbound.repository_contracts;

import com.ros.lms.domain.entities.Genre;
import com.ros.lms.domain.enums.GenreType;

import java.util.Optional;
import java.util.Set;

public interface GenreDAO {
    Optional<Genre> findByLabel(GenreType genreType);
    Set<GenreType> findAll();
}
