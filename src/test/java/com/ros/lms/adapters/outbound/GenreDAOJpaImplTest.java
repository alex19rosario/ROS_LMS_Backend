package com.ros.lms.adapters.outbound;

import com.ros.lms.adapters.outbound.repositories.GenreDAOJpaImpl;
import com.ros.lms.domain.entities.Genre;
import com.ros.lms.domain.enums.GenreType;
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
        genre.setDescription(GenreType.TECHNOLOGY);
        entityManager.persist(genre);
        entityManager.flush();

        // Act
        Optional<Genre> result = genreDAO.findByDescription(GenreType.TECHNOLOGY);

        // Assert
        assertTrue(result.isPresent(), "Genre should be found");
        assertEquals(GenreType.TECHNOLOGY, result.get().getDescription(), "Description should match");
    }

    @Test
    void testFindByDescription_noResult() {
        // Act
        Optional<Genre> result = genreDAO.findByDescription(GenreType.ADVENTURE);
        // Assert
        assertThat(result).isEmpty();
    }

    @Transactional
    @Test
    void testFindAllWhenGenresExist() {
        // Arrange
        Genre genre1 = new Genre(GenreType.TECHNOLOGY);
        Genre genre2 = new Genre(GenreType.SCIENCE);
        Genre genre3 = new Genre(GenreType.ADVENTURE);

        entityManager.persist(genre1);
        entityManager.persist(genre2);
        entityManager.persist(genre3);
        entityManager.flush();

        // Act
        Set<GenreType> result = genreDAO.findAll();

        // Assert
        assertThat(result).contains(GenreType.TECHNOLOGY, GenreType.SCIENCE, GenreType.ADVENTURE);
    }

}
