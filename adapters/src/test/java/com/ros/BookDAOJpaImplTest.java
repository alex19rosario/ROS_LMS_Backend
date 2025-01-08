package com.ros;

import com.ros.entities.Book;
import com.ros.outbound.repositories.BookDAOJpaImpl;
import com.ros.ports_outbound.dao.BookDAO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BookDAOJpaImplTest {
    @Autowired
    private EntityManager entityManager;

    private BookDAO bookDAO;

    @BeforeEach
    void setUp() {
        bookDAO = new BookDAOJpaImpl(entityManager);
    }

    @Transactional
    @Test
    void create_shouldPersistBook() {
        // Arrange
        Book book = new Book(9783161484100L, "Effective Java", 'Y');

        // Act
        bookDAO.create(book);
        entityManager.flush(); // Ensure the entity is persisted

        // Retrieve the generated ID
        long generatedId = book.getId();

        // Assert
        Book persistedBook = entityManager.find(Book.class, generatedId); // Use the primary key (id)
        assertThat(persistedBook).isNotNull();
        assertThat(persistedBook.getISBN()).isEqualTo(9783161484100L);
        assertThat(persistedBook.getTitle()).isEqualTo("Effective Java");
    }

    @Transactional
    @Test
    void findByISBN_shouldReturnBook_whenBookExists() {
        // Arrange
        Book book = new Book(9783161484100L, "Effective Java", 'Y');
        entityManager.persist(book); // Pre-populate the database

        // Act
        Optional<Book> foundBook = bookDAO.findByISBN(9783161484100L);

        // Assert
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("Effective Java");
    }

    @Test
    void findByISBN_shouldReturnEmptyOptional_whenBookDoesNotExist() {
        // Act
        Optional<Book> foundBook = bookDAO.findByISBN(1234567890123L);

        // Assert
        assertThat(foundBook).isEmpty();
    }

}
