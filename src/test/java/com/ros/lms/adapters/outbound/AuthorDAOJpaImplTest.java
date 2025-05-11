package com.ros.lms.adapters.outbound;

import com.ros.lms.adapters.outbound.repositories.AuthorDAOJpaImpl;
import com.ros.lms.domain.entities.Author;
import com.ros.lms.ports.outbound.repository_contracts.AuthorDAO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test") // This activates application-test.properties
public class AuthorDAOJpaImplTest {

    @Autowired
    private EntityManager entityManager;

    private AuthorDAO authorDAO;

    @BeforeEach
    void setUp() {
        authorDAO = new AuthorDAOJpaImpl(entityManager);
    }

    @Transactional
    @Test
    void testFindByFullName_withFirstAndLastNameOnly() {
        // Arrange
        Author author = new Author();
        author.setFirstName("John");
        author.setLastName("Doe");
        entityManager.persist(author);
        entityManager.flush();

        // Act
        Optional<Author> result = authorDAO.findByFullName("John", null, "Doe");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("John");
        assertThat(result.get().getLastName()).isEqualTo("Doe");
    }

    @Transactional
    @Test
    void testFindByFullName_withFirstMiddleAndLastName() {
        // Arrange
        Author author = new Author();
        author.setFirstName("Jane");
        author.setMiddleName("A.");
        author.setLastName("Smith");
        entityManager.persist(author);
        entityManager.flush();

        // Act
        Optional<Author> result = authorDAO.findByFullName("Jane", "A.", "Smith");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("Jane");
        assertThat(result.get().getMiddleName()).isEqualTo("A.");
        assertThat(result.get().getLastName()).isEqualTo("Smith");
    }

    @Test
    void testFindByFullName_noResult() {
        // Act
        Optional<Author> result = authorDAO.findByFullName("Nonexistent", null, "Author");

        // Assert
        assertThat(result).isEmpty();
    }

    @Transactional
    @Test
    void testFindByFirstAndLastName() {
        // Arrange
        Author author = new Author();
        author.setFirstName("Alice");
        author.setLastName("Johnson");
        entityManager.persist(author);
        entityManager.flush();

        // Act
        Optional<Author> result = authorDAO.findByFirstAndLastName("Alice", "Johnson");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("Alice");
        assertThat(result.get().getLastName()).isEqualTo("Johnson");
    }

    @Test
    void testFindByFirstAndMiddleAndLastName_noResult() {
        // Act
        Optional<Author> result = authorDAO.findByFirstAndMiddleAndLastName("Nonexistent", "M.", "Author");

        // Assert
        assertThat(result).isEmpty();
    }

}
