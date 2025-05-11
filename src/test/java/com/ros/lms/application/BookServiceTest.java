package com.ros.lms.application;

import com.ros.lms.domain.dtos.AddBookDTO;
import com.ros.lms.domain.dtos.AuthorDTO;
import com.ros.lms.domain.entities.Author;
import com.ros.lms.domain.entities.Book;
import com.ros.lms.domain.entities.Genre;
import com.ros.lms.domain.exceptions.BookAlreadyExistsException;
import com.ros.lms.ports.outbound.repository_contracts.AuthorDAO;
import com.ros.lms.ports.outbound.repository_contracts.BookDAO;
import com.ros.lms.ports.outbound.repository_contracts.GenreDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class BookServiceTest {
    @Mock
    private BookDAO bookDAO;

    @Mock
    private AuthorDAO authorDAO;

    @Mock
    private GenreDAO genreDAO;

    @InjectMocks
    private BookServiceImpl bookService;

    private AddBookDTO validBookDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a sample AddBookDTO
        AuthorDTO author = new AuthorDTO("Joshua", "Bloch");
        validBookDTO = new AddBookDTO(
                9783161484105L,
                "Effective Java",
                Set.of(author),
                Set.of("SCIENCE")
        );
    }

    @Test
    void save_shouldThrowException_whenBookAlreadyExists() {
        // Arrange
        when(bookDAO.findByISBN(validBookDTO.ISBN())).thenReturn(Optional.of(new Book()));

        // Act & Assert
        assertThrows(BookAlreadyExistsException.class, () -> bookService.add(validBookDTO));

        // Verify that no further interactions occur
        verify(bookDAO).findByISBN(validBookDTO.ISBN());
        verifyNoMoreInteractions(bookDAO, authorDAO, genreDAO);
    }

    @Test
    void save_shouldSaveBook_whenBookDoesNotExist() throws BookAlreadyExistsException {
        // Arrange
        when(bookDAO.findByISBN(validBookDTO.ISBN())).thenReturn(Optional.empty());
        when(authorDAO.findByFullName("Joshua", null, "Bloch")).thenReturn(Optional.empty());
        when(genreDAO.findByDescription("SCIENCE")).thenReturn(Optional.of(new Genre("SCIENCE")));

        // Act
        bookService.add(validBookDTO);

        // Assert
        verify(bookDAO).findByISBN(validBookDTO.ISBN());
        verify(authorDAO).findByFullName("Joshua", null, "Bloch");
        verify(genreDAO).findByDescription("SCIENCE");
        verify(bookDAO).create(any(Book.class));
    }

    @Test
    void save_shouldAssociateExistingAuthor_whenAuthorExists() throws BookAlreadyExistsException {
        // Arrange
        Author existingAuthor = new Author("Joshua", "", "Bloch");
        when(bookDAO.findByISBN(validBookDTO.ISBN())).thenReturn(Optional.empty());
        when(authorDAO.findByFullName("Joshua", null, "Bloch")).thenReturn(Optional.of(existingAuthor));
        when(genreDAO.findByDescription("SCIENCE")).thenReturn(Optional.of(new Genre("SCIENCE")));

        // Act
        bookService.add(validBookDTO);

        // Assert
        verify(authorDAO).findByFullName("Joshua", null, "Bloch");
        verify(bookDAO).create(any(Book.class));
        verify(genreDAO).findByDescription("SCIENCE");
    }
}
