package com.ros.lms.adapters.outbound;

import com.ros.lms.adapters.outbound.repositories.GenreDAOJpaImpl;
import com.ros.lms.domain.entities.Genre;
import com.ros.lms.ports.outbound.repository_contracts.GenreDAO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // This activates application-test.properties
public class GenreDAOJpaImplTest {
    @Autowired
    private EntityManager entityManager;

    private GenreDAO genreDAO;

    @BeforeEach
    void setUp(){
        genreDAO = new GenreDAOJpaImpl(entityManager);
    }

    @Transactional
    @Test
    void testFindByDescriptionWhenGenreExists() {
        // Arrange
        Genre genre = new Genre();
        genre.setDescription("test genre");
        entityManager.persist(genre);
        entityManager.flush();

        // Act
        Optional<Genre> result = genreDAO.findByDescription("test genre");

        // Assert
        assertTrue(result.isPresent(), "Genre should be found");
        assertEquals("test genre", result.get().getDescription(), "Description should match");
    }

    @Test
    void testFindByDescription_noResult() {
        // Act
        Optional<Genre> result = genreDAO.findByDescription("Nonexistent");
        // Assert
        assertThat(result).isEmpty();
    }

    @Transactional
    @Test
    void testFindAllWhenGenresExist() {
        // Arrange
        Genre genre1 = new Genre();
        genre1.setDescription("test genre 1");
        Genre genre2 = new Genre();
        genre2.setDescription("test genre 2");
        Genre genre3 = new Genre();
        genre3.setDescription("test genre 3");

        entityManager.persist(genre1);
        entityManager.persist(genre2);
        entityManager.persist(genre3);
        entityManager.flush();

        // Act
        Set<String> result = genreDAO.findAll();

        // Assert
        assertNotEquals(0, result.size());
    }

}
