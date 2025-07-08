package com.ros.lms.adapters.outbound;

import com.ros.lms.adapters.outbound.repositories.BookDAOJpaImpl;
import com.ros.lms.domain.entities.Author;
import com.ros.lms.domain.entities.Book;
import com.ros.lms.domain.entities.Genre;
import com.ros.lms.ports.outbound.repository_contracts.BookDAO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test") // This activates application-test.properties
public class BookDAOJpaImplTest {
    @Autowired
    private EntityManager entityManager;

    private BookDAO bookDAO;

    @BeforeEach
    void setUp() {
        bookDAO = new BookDAOJpaImpl(entityManager);
    }


    @Test
    @Transactional
    void create_shouldPersistBook() {
        // Arrange
        Book book = new Book(9783161484100L, "Effective Java", true);

        // Act
        bookDAO.create(book);
        entityManager.flush(); // Ensure the entity is persisted

        // Retrieve the generated ID
        long generatedId = book.getId();

        // Assert
        Book persistedBook = entityManager.find(Book.class, generatedId); // Use the primary key (id)
        assertThat(persistedBook).isNotNull();
        assertThat(persistedBook.getIsbn()).isEqualTo(9783161484100L);
        assertThat(persistedBook.getTitle()).isEqualTo("Effective Java");
    }

    @Transactional
    @Test
    void findByISBN_shouldReturnBook_whenBookExists() {
        // Arrange
        Book book = new Book(9783161484100L, "Effective Java", true);
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

    @Test
    @Transactional
    void findAllOrderedByTitle_shouldReturnFilteredBooks_byTitle() {
        // Arrange
        Book book1 = new Book(111L, "Effective Java", true);
        Book book2 = new Book(222L, "Clean Code", true);
        entityManager.persist(book1);
        entityManager.persist(book2);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Book> result = bookDAO.findAllOrderedByTitle("Clean", null, null, null, pageable);

        // Assert
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getTitle()).isEqualTo("Clean Code");
    }

    @Test
    @Transactional
    void findAllOrderedByTitle_shouldReturnFilteredBooks_byGenre() {
        // Arrange
        Genre genreTech = new Genre("TECHNOLOGY");
        Genre genreSci = new Genre("SCIENCE");

        Book book1 = new Book(111L, "Clean Code", true);
        book1.addGenre(genreTech);

        Book book2 = new Book(222L, "Physics Fundamentals", true);
        book2.addGenre(genreSci);

        entityManager.persist(genreTech);
        entityManager.persist(genreSci);
        entityManager.persist(book1);
        entityManager.persist(book2);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Book> result = bookDAO.findAllOrderedByTitle(null, "TECH", null, null, pageable);

        // Assert
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getTitle()).isEqualTo("Clean Code");
    }

    @Test
    @Transactional
    void findAllOrderedByTitle_shouldReturnFilteredBooks_byAuthorName() {
        // Arrange
        Author author1 = new Author("Joshua", null, "Bloch");
        Author author2 = new Author("Robert", "C.", "Martin");

        Book book1 = new Book(111L, "Effective Java", true);
        book1.addAuthor(author1);

        Book book2 = new Book(222L, "Clean Code", true);
        book2.addAuthor(author2);

        entityManager.persist(author1);
        entityManager.persist(author2);
        entityManager.persist(book1);
        entityManager.persist(book2);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Book> result = bookDAO.findAllOrderedByTitle(null, null, "Joshua", null, pageable);

        // Assert
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getTitle()).isEqualTo("Effective Java");
    }

    @Test
    @Transactional
    void findAllOrderedByTitle_shouldReturnAllBooks_whenNoFilters() {
        // Arrange
        Book book1 = new Book(111L, "Book One", true);
        Book book2 = new Book(222L, "Book Two", true);
        entityManager.persist(book1);
        entityManager.persist(book2);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Book> result = bookDAO.findAllOrderedByTitle(null, null, null, null, pageable);

        // Assert
        assertThat(result.getTotalElements()).isEqualTo(2);
        List<String> titles = result.getContent().stream().map(Book::getTitle).toList();
        assertThat(titles).containsExactlyInAnyOrder("Book One", "Book Two");
    }

    @Test
    @Transactional
    void findAllOrderedByTitle_shouldReturnFilteredBooks_byAuthorLastName() {
        // Arrange
        Author author1 = new Author("Joshua", null, "Bloch");
        Author author2 = new Author("Robert", "C.", "Martin");

        Book book1 = new Book(111L, "Effective Java", true);
        book1.addAuthor(author1);

        Book book2 = new Book(222L, "Clean Code", true);
        book2.addAuthor(author2);

        entityManager.persist(author1);
        entityManager.persist(author2);
        entityManager.persist(book1);
        entityManager.persist(book2);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Book> result = bookDAO.findAllOrderedByTitle(null, null, null, "Bloch", pageable);

        // Assert
        assertThat(result.getTotalElements()).isEqualTo(1);
        Book foundBook = result.getContent().getFirst();
        assertThat(foundBook.getTitle()).isEqualTo("Effective Java");
        assertThat(foundBook.getAuthors().getFirst().getLastName()).isEqualTo("Bloch");
    }

}
