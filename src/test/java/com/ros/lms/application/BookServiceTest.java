package com.ros.lms.application;

import com.ros.lms.domain.dtos.AddBookDTO;
import com.ros.lms.domain.dtos.AuthorDTO;
import com.ros.lms.domain.entities.Author;
import com.ros.lms.domain.entities.Book;
import com.ros.lms.domain.entities.Genre;
import com.ros.lms.domain.exceptions.BookAlreadyExistsException;
import com.ros.lms.domain.exceptions.StorageException;
import com.ros.lms.ports.inbound.service_contracts.StorageService;
import com.ros.lms.ports.outbound.repository_contracts.AuthorDAO;
import com.ros.lms.ports.outbound.repository_contracts.BookDAO;
import com.ros.lms.ports.outbound.repository_contracts.GenreDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class BookServiceTest {
    @Mock
    private BookDAO bookDAO;

    @Mock
    private AuthorDAO authorDAO;

    @Mock
    private GenreDAO genreDAO;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private BookServiceImpl bookService;

    private AddBookDTO validBookDTO;

    private MockMultipartFile coverImage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        coverImage = new MockMultipartFile(
                "coverImage",
                "cover.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "dummy-image-data".getBytes()
        );
        // Create a sample AddBookDTO
        validBookDTO = new AddBookDTO(
                9783161484105L,
                "Effective Java",
                "Carlos Alexander-Rosario,Joshua-Bloch",
                "SCIENCE,TECHNOLOGY",
                coverImage
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
    void save_shouldSaveBook_whenBookDoesNotExist() throws BookAlreadyExistsException, StorageException {
        // Arrange
        when(bookDAO.findByISBN(validBookDTO.ISBN())).thenReturn(Optional.empty());
        when(authorDAO.findByFullName("Carlos", "Alexander", "Rosario")).thenReturn(Optional.empty());
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
    void save_shouldAssociateExistingAuthor_whenAuthorExists() throws BookAlreadyExistsException, StorageException {
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

    @Test
    void save_shouldThrowStorageException_whenCoverImageStorageFails() throws StorageException {
        // Arrange
        when(bookDAO.findByISBN(validBookDTO.ISBN())).thenReturn(Optional.empty());
        when(authorDAO.findByFullName("Carlos", "Alexander", "Rosario")).thenReturn(Optional.empty());
        when(authorDAO.findByFullName("Joshua", null, "Bloch")).thenReturn(Optional.empty());
        when(genreDAO.findByDescription("SCIENCE")).thenReturn(Optional.of(new Genre("SCIENCE")));
        when(genreDAO.findByDescription("TECHNOLOGY")).thenReturn(Optional.of(new Genre("TECHNOLOGY")));

        // Simulate failure during storage
        doThrow(new RuntimeException("Disk full")).when(storageService).store(any());

        // Act & Assert
        StorageException exception = assertThrows(StorageException.class, () -> bookService.add(validBookDTO));
        assert(exception.getMessage().contains("Failed to store cover image"));

        // Verify that store was attempted
        verify(storageService).store(any());
        verify(bookDAO, never()).create(any(Book.class)); // Book shouldn't be saved
    }

    @Test
    void save_shouldThrowIllegalArgumentException_whenAuthorNameIsInvalid() {
        // Arrange
        AddBookDTO invalidAuthorDTO = new AddBookDTO(
                9783161484107L,
                "Refactoring",
                "Martin Fowler", // Invalid format (missing hyphen)
                "SOFTWARE",
                null
        );

        when(bookDAO.findByISBN(invalidAuthorDTO.ISBN())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> bookService.add(invalidAuthorDTO));

        // Verify no interactions with DAOs (since parsing fails early)
        verify(bookDAO).findByISBN(invalidAuthorDTO.ISBN());
        verifyNoMoreInteractions(bookDAO, authorDAO, genreDAO, storageService);
    }

    @Test
    void save_shouldSaveBookWithoutCoverImage() throws BookAlreadyExistsException, StorageException {
        // Arrange
        AddBookDTO noCoverImageDTO = new AddBookDTO(
                9783161484106L,
                "Clean Code",
                "Robert-C. Martin",
                "TECHNOLOGY",
                null // No cover image
        );

        when(bookDAO.findByISBN(noCoverImageDTO.ISBN())).thenReturn(Optional.empty());
        when(authorDAO.findByFullName("Robert", "C.", "Martin")).thenReturn(Optional.empty());
        when(genreDAO.findByDescription("TECHNOLOGY")).thenReturn(Optional.of(new Genre("TECHNOLOGY")));

        // Act
        bookService.add(noCoverImageDTO);

        // Assert
        verify(bookDAO).create(any(Book.class));
        verify(storageService, never()).store(any()); // Ensure store wasn't called
    }

    @Test
    void save_shouldThrowException_whenAuthorFormatIsInvalid() {
        // Arrange
        AddBookDTO invalidAuthorFormatDTO = new AddBookDTO(
                9783161484107L,
                "Refactoring",
                "MartinFowler", // Invalid format (missing -)
                "SOFTWARE",
                null
        );

        when(bookDAO.findByISBN(invalidAuthorFormatDTO.ISBN())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> bookService.add(invalidAuthorFormatDTO));

        // Book should not be saved
        verify(bookDAO, never()).create(any(Book.class));
    }




}
