package com.ros.lms.adapters.outbound;

import com.ros.lms.adapters.outbound.repositories.BookDAOJpaImpl;
import com.ros.lms.domain.entities.Author;
import com.ros.lms.domain.entities.Book;
import com.ros.lms.domain.entities.Genre;
import com.ros.lms.domain.enums.GenreType;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        Book book = new Book("9783161484100", "Effective Java", true);

        // Act
        bookDAO.create(book);
        entityManager.flush(); // Ensure the entity is persisted

        // Retrieve the generated ID
        long generatedId = book.getId();

        // Assert
        Book persistedBook = entityManager.find(Book.class, generatedId); // Use the primary key (id)
        assertThat(persistedBook).isNotNull();
        assertThat(persistedBook.getIsbn()).isEqualTo("9783161484100");
        assertThat(persistedBook.getTitle()).isEqualTo("Effective Java");
    }

    @Transactional
    @Test
    void findByISBN_shouldReturnBook_whenBookExists() {
        // Arrange
        Book book = new Book("9783161484100", "Effective Java", true);
        entityManager.persist(book); // Pre-populate the database

        // Act
        Optional<Book> foundBook = bookDAO.findByISBN("9783161484100");

        // Assert
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("Effective Java");
    }

    @Test
    void findByISBN_shouldReturnEmptyOptional_whenBookDoesNotExist() {
        // Act
        Optional<Book> foundBook = bookDAO.findByISBN("1234567890123");

        // Assert
        assertThat(foundBook).isEmpty();
    }

    @Test
    @Transactional
    void findAllOrderedByTitle_shouldReturnFilteredBooks_byTitle() {
        // Arrange
        Book book1 = new Book("1113216543214", "Effective Java", true);
        Book book2 = new Book("2229876543212", "Clean Code", true);
        entityManager.persist(book1);
        entityManager.persist(book2);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Book> result = bookDAO.findAllOrderedByTitle("Clean", null, null, null, null, pageable);

        // Assert
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getTitle()).isEqualTo("Clean Code");
    }

    @Test
    @Transactional
    void findAllOrderedByTitle_shouldReturnFilteredBooks_byGenre() {
        // Arrange
        Genre genreTech = new Genre(GenreType.TECHNOLOGY);
        Genre genreSci = new Genre(GenreType.SCIENCE);

        Book book1 = new Book("3216549876541", "Clean Code", true);
        book1.addGenre(genreTech);

        Book book2 = new Book("6543216549872", "Physics Fundamentals", true);
        book2.addGenre(genreSci);

        entityManager.persist(genreTech);
        entityManager.persist(genreSci);
        entityManager.persist(book1);
        entityManager.persist(book2);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Book> result = bookDAO.findAllOrderedByTitle(null, GenreType.TECHNOLOGY, null, null, null, pageable);

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

        Book book1 = new Book("3216549877", "Effective Java", true);
        book1.addAuthor(author1);

        Book book2 = new Book("4567891235", "Clean Code", true);
        book2.addAuthor(author2);

        entityManager.persist(author1);
        entityManager.persist(author2);
        entityManager.persist(book1);
        entityManager.persist(book2);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Book> result = bookDAO.findAllOrderedByTitle(null, null, "Joshua", null, null, pageable);

        // Assert
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getTitle()).isEqualTo("Effective Java");
    }

    @Test
    @Transactional
    void findAllOrderedByTitle_shouldReturnAllBooks_whenNoFilters() {
        // Arrange
        Book book1 = new Book("6547417895", "Book One", true);
        Book book2 = new Book("9638527415", "Book Two", true);
        entityManager.persist(book1);
        entityManager.persist(book2);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Book> result = bookDAO.findAllOrderedByTitle(null, null, null, null, null, pageable);

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

        Book book1 = new Book("9638527415", "Effective Java", true);
        book1.addAuthor(author1);

        Book book2 = new Book("65498774165", "Clean Code", true);
        book2.addAuthor(author2);

        entityManager.persist(author1);
        entityManager.persist(author2);
        entityManager.persist(book1);
        entityManager.persist(book2);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Book> result = bookDAO.findAllOrderedByTitle(null, null, null, "Bloch", null, pageable);

        // Assert
        assertThat(result.getTotalElements()).isEqualTo(1);
        Book foundBook = result.getContent().getFirst();
        assertThat(foundBook.getTitle()).isEqualTo("Effective Java");
        assertThat(foundBook.getAuthors().getFirst().getLastName()).isEqualTo("Bloch");
    }

    @Test
    @Transactional
    void findAllOrderedByTitle_shouldReturnOnlyAvailableBooks() {
        // Arrange
        Book availableBook = new Book("9638527415", "Available Book", true);
        Book unavailableBook = new Book("5555555556", "Unavailable Book", false);
        entityManager.persist(availableBook);
        entityManager.persist(unavailableBook);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Book> result = bookDAO.findAllOrderedByTitle(null, null, null, null, true, pageable);

        // Assert
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getTitle()).isEqualTo("Available Book");
    }

    @Test
    @Transactional
    void findAllOrderedByTitle_shouldReturnOnlyUnavailableBooks() {
        // Arrange
        Book availableBook = new Book("6547894561", "Available Book", true);
        Book unavailableBook = new Book("6549873215", "Unavailable Book", false);
        entityManager.persist(availableBook);
        entityManager.persist(unavailableBook);

        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<Book> result = bookDAO.findAllOrderedByTitle(null, null, null, null, false, pageable);

        // Assert
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().getFirst().getTitle()).isEqualTo("Unavailable Book");
    }


    @Test
    @Transactional
    void findById_shouldReturnBook_whenBookExists() {
        // Arrange
        Book book = new Book("9783161484100", "Effective Java", true);
        entityManager.persist(book);
        entityManager.flush();

        // Act
        Optional<Book> foundBook = bookDAO.findById(book.getId());

        // Assert
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("Effective Java");
    }

    @Test
    @Transactional
    void findById_shouldReturnEmpty_whenBookDoesNotExist() {
        // Act
        Optional<Book> foundBook = bookDAO.findById(999L);

        // Assert
        assertThat(foundBook).isNotPresent();
    }

    @Test
    @Transactional
    void update_shouldMergeChangesToExistingBook() {
        // Arrange
        Book book = new Book("9783161484100", "Effective Java", true);
        entityManager.persist(book);
        entityManager.flush();
        entityManager.clear(); // Detach all entities to simulate real update

        // Act
        Book updatedBook = new Book("9783161484100", "Effective Java - 3rd Edition", true);
        updatedBook.setId(book.getId());
        bookDAO.update(updatedBook);
        entityManager.flush();

        // Assert
        Book mergedBook = entityManager.find(Book.class, book.getId());
        assertThat(mergedBook.getTitle()).isEqualTo("Effective Java - 3rd Edition");
    }

    @Test
    void findById_shouldReturnEmpty_whenExceptionOccurs() {
        // Arrange
        EntityManager mockEm = mock(EntityManager.class);
        BookDAOJpaImpl dao = new BookDAOJpaImpl(mockEm);

        long invalidId = 999L;

        // Simulate failure in entityManager.find
        when(mockEm.find(Book.class, invalidId)).thenThrow(new RuntimeException("DB failure"));

        // Act
        Optional<Book> result = dao.findById(invalidId);

        // Assert
        assertThat(result).isEmpty();
    }

}
