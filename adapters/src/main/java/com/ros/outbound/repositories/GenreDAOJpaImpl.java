package com.ros.outbound.repositories;

import com.ros.entities.Author;
import com.ros.entities.Genre;
import com.ros.ports_outbound.dao.GenreDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GenreDAOJpaImpl implements GenreDAO {

    private final EntityManager entityManager;

    @Autowired
    public GenreDAOJpaImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Genre> findByDescription(String description) {
        String query = "SELECT g FROM Genre g WHERE g.description = :description";
        try {
            Genre genre = entityManager.createQuery(query, Genre.class)
                    .setParameter("description", description)
                    .getSingleResult();

            return Optional.ofNullable(genre);

        } catch (NoResultException e){
            return Optional.empty();
        }
    }
}
