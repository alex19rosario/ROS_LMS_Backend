package com.ros;

import com.ros.entities.Genre;
import com.ros.outbound.repositories.GenreDAOJpaImpl;
import com.ros.ports_outbound.dao.GenreDAO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

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
        genre.setDescription("Fiction");
        entityManager.persist(genre);
        entityManager.flush();

        // Act
        Optional<Genre> result = genreDAO.findByDescription("Fiction");

        // Assert
        assertTrue(result.isPresent(), "Genre should be found");
        assertEquals("Fiction", result.get().getDescription(), "Description should match");
    }

    @Transactional
    @Test
    void testFindAllWhenGenresExist() {
        // Arrange
        Genre genre1 = new Genre();
        genre1.setDescription("Fiction");
        Genre genre2 = new Genre();
        genre2.setDescription("Non-Fiction");
        Genre genre3 = new Genre();
        genre3.setDescription("Science");

        entityManager.persist(genre1);
        entityManager.persist(genre2);
        entityManager.persist(genre3);
        entityManager.flush();

        // Act
        Set<String> result = genreDAO.findAll();

        // Assert
        assertEquals(3, result.size(), "There should be 3 genres");
        assertTrue(result.contains("Fiction"), "Fiction should be in the results");
        assertTrue(result.contains("Non-Fiction"), "Non-Fiction should be in the results");
        assertTrue(result.contains("Science"), "Science should be in the results");
    }

}
