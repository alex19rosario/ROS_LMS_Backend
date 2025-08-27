package com.ros.lms.application;

import com.ros.lms.domain.dtos.AddBookDTO;
import com.ros.lms.domain.dtos.BookDTO;
import com.ros.lms.domain.dtos.SearchBookDTO;
import com.ros.lms.domain.entities.Author;
import com.ros.lms.domain.entities.Book;
import com.ros.lms.domain.entities.Genre;
import com.ros.lms.domain.entities.Staff;
import com.ros.lms.domain.enums.GenreType;
import com.ros.lms.domain.exceptions.*;
import com.ros.lms.ports.inbound.service_contracts.StorageService;
import com.ros.lms.ports.outbound.repository_contracts.AuthorDAO;
import com.ros.lms.ports.outbound.repository_contracts.BookDAO;
import com.ros.lms.ports.outbound.repository_contracts.GenreDAO;
import com.ros.lms.ports.outbound.repository_contracts.StaffDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {
    @Mock
    private BookDAO bookDAO;

    @Mock
    private AuthorDAO authorDAO;

    @Mock
    private GenreDAO genreDAO;

    @Mock
    private StaffDAO staffDAO;

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
                "9783161484105",
                "Effective Java",
                "Carlos Alexander-Rosario,Joshua-Bloch",
                "SCIENCE,TECHNOLOGY",
                "staff",
                coverImage
        );
    }

    @Test
    void save_shouldThrowException_whenBookAlreadyExists() {
        // Arrange
        when(bookDAO.findByISBN(validBookDTO.isbn())).thenReturn(Optional.of(new Book()));

        // Act & Assert
        assertThrows(BookAlreadyExistsException.class, () -> bookService.add(validBookDTO));

        // Verify that no further interactions occur
        verify(bookDAO).findByISBN(validBookDTO.isbn());
        verifyNoMoreInteractions(bookDAO, authorDAO, genreDAO);
    }

    @Test
    void save_shouldSaveBook_whenBookDoesNotExist() throws BookAlreadyExistsException, StorageException, StaffNotFoundException {
        // Arrange
        when(bookDAO.findByISBN(validBookDTO.isbn())).thenReturn(Optional.empty());
        when(authorDAO.findByFullName("Carlos", "Alexander", "Rosario")).thenReturn(Optional.empty());
        when(genreDAO.findByLabel(GenreType.SCIENCE)).thenReturn(Optional.of(new Genre(GenreType.SCIENCE)));
        when(staffDAO.findByUsername(validBookDTO.staffUsername())).thenReturn(Optional.of(new Staff()));
        // Act
        bookService.add(validBookDTO);

        // Assert
        verify(bookDAO).findByISBN(validBookDTO.isbn());
        verify(authorDAO).findByFullName("Joshua", null, "Bloch");
        verify(genreDAO).findByLabel(GenreType.SCIENCE);
        verify(bookDAO).create(any(Book.class));
    }

    @Test
    void save_shouldAssociateExistingAuthor_whenAuthorExists() throws BookAlreadyExistsException, StorageException, StaffNotFoundException {
        // Arrange
        Author existingAuthor = new Author("Joshua", "", "Bloch");
        when(bookDAO.findByISBN(validBookDTO.isbn())).thenReturn(Optional.empty());
        when(authorDAO.findByFullName("Joshua", null, "Bloch")).thenReturn(Optional.of(existingAuthor));
        when(genreDAO.findByLabel(GenreType.SCIENCE)).thenReturn(Optional.of(new Genre(GenreType.SCIENCE)));
        when(staffDAO.findByUsername(validBookDTO.staffUsername())).thenReturn(Optional.of(new Staff()));

        // Act
        bookService.add(validBookDTO);

        // Assert
        verify(authorDAO).findByFullName("Joshua", null, "Bloch");
        verify(bookDAO).create(any(Book.class));
        verify(genreDAO).findByLabel(GenreType.SCIENCE);
    }

    @Test
    void save_shouldThrowStorageException_whenCoverImageStorageFails() throws StorageException {
        // Arrange
        when(bookDAO.findByISBN(validBookDTO.isbn())).thenReturn(Optional.empty());
        when(authorDAO.findByFullName("Carlos", "Alexander", "Rosario")).thenReturn(Optional.empty());
        when(authorDAO.findByFullName("Joshua", null, "Bloch")).thenReturn(Optional.empty());
        when(genreDAO.findByLabel(GenreType.SCIENCE)).thenReturn(Optional.of(new Genre(GenreType.SCIENCE)));
        when(genreDAO.findByLabel(GenreType.TECHNOLOGY)).thenReturn(Optional.of(new Genre(GenreType.TECHNOLOGY)));
        when(staffDAO.findByUsername(validBookDTO.staffUsername())).thenReturn(Optional.of(new Staff()));

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
                "9783161484107",
                "Refactoring",
                "Martin Fowler", // Invalid format (missing hyphen)
                "SOFTWARE",
                "staff",
                null
        );

        when(bookDAO.findByISBN(invalidAuthorDTO.isbn())).thenReturn(Optional.empty());
        when(staffDAO.findByUsername(validBookDTO.staffUsername())).thenReturn(Optional.of(new Staff()));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> bookService.add(invalidAuthorDTO));

        // Verify no interactions with DAOs (since parsing fails early)
        verify(bookDAO).findByISBN(invalidAuthorDTO.isbn());
        verifyNoMoreInteractions(bookDAO, authorDAO, genreDAO, storageService);
    }

    @Test
    void save_shouldSaveBookWithoutCoverImage() throws BookAlreadyExistsException, StorageException, StaffNotFoundException {
        // Arrange
        AddBookDTO noCoverImageDTO = new AddBookDTO(
                "9783161484106",
                "Clean Code",
                "Robert-C. Martin",
                "TECHNOLOGY",
                "staff",
                null // No cover image
        );

        when(bookDAO.findByISBN(noCoverImageDTO.isbn())).thenReturn(Optional.empty());
        when(authorDAO.findByFullName("Robert", "C.", "Martin")).thenReturn(Optional.empty());
        when(genreDAO.findByLabel(GenreType.TECHNOLOGY)).thenReturn(Optional.of(new Genre(GenreType.TECHNOLOGY)));
        when(staffDAO.findByUsername(validBookDTO.staffUsername())).thenReturn(Optional.of(new Staff()));

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
                "9783161484107",
                "Refactoring",
                "MartinFowler", // Invalid format (missing -)
                "SOFTWARE",
                "staff",
                null
        );

        when(bookDAO.findByISBN(invalidAuthorFormatDTO.isbn())).thenReturn(Optional.empty());
        when(staffDAO.findByUsername(validBookDTO.staffUsername())).thenReturn(Optional.of(new Staff()));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> bookService.add(invalidAuthorFormatDTO));

        // Book should not be saved
        verify(bookDAO, never()).create(any(Book.class));
    }

    @Test
    void save_shouldThrowIllegalArgumentException_whenGenreIsInvalid() {
        // Arrange
        AddBookDTO invalidGenreDTO = new AddBookDTO(
                "9783161484110",
                "Domain-Driven Design",
                "Eric-Evans",
                "INVALID_GENRE", // Invalid genre
                "staff",
                null
        );

        when(bookDAO.findByISBN(invalidGenreDTO.isbn())).thenReturn(Optional.empty());
        when(authorDAO.findByFullName("Eric", null, "Evans")).thenReturn(Optional.empty());
        when(staffDAO.findByUsername(validBookDTO.staffUsername())).thenReturn(Optional.of(new Staff()));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> bookService.add(invalidGenreDTO));

        assertThat(exception.getMessage()).isEqualTo("Invalid genre provided: INVALID_GENRE");

        verify(bookDAO).findByISBN(invalidGenreDTO.isbn());
        verify(authorDAO).findByFullName("Eric", null, "Evans");
        verifyNoInteractions(genreDAO, storageService); // These should still not be called
    }

    @Test
    void save_shouldThrowStaffNotFoundException_whenStaffDoesNotExist() {
        // Arrange
        when(bookDAO.findByISBN(validBookDTO.isbn())).thenReturn(Optional.empty());
        when(staffDAO.findByUsername(validBookDTO.staffUsername())).thenReturn(Optional.empty());

        // Act & Assert
        StaffNotFoundException exception = assertThrows(StaffNotFoundException.class,
                () -> bookService.add(validBookDTO));

        assertThat(exception.getMessage()).contains("Staff not found");

        // Ensure no further DAO methods are called
        verify(bookDAO).findByISBN(validBookDTO.isbn());
        verify(staffDAO).findByUsername(validBookDTO.staffUsername());
        verifyNoMoreInteractions(bookDAO, authorDAO, genreDAO, storageService);
    }


    @Test
    void getAll_shouldReturnMappedDTOs_whenInputsAreValid() throws PageOutOfRangeException {
        Book book = new Book("9783161484105", "Effective Java", true);
        book.setId(1L);

        Page<Book> bookPage = new PageImpl<>(List.of(book));
        SearchBookDTO search = new SearchBookDTO(0, 10, "Java", GenreType.TECHNOLOGY, "Joshua", "Bloch", null);

        when(bookDAO.findAllOrderedByTitle(eq("Java"), eq(GenreType.TECHNOLOGY), eq("Joshua"), eq("Bloch"), eq(null), any(Pageable.class)))
                .thenReturn(bookPage);

        Page<BookDTO> result = bookService.getAll(search);

        assertEquals(1, result.getTotalElements());
        BookDTO dto = result.getContent().getFirst();
        assertEquals("Effective Java", dto.title());
        assertEquals("9783161484105", dto.isbn());
        assertTrue(dto.status());
    }

    @Test
    void getAll_shouldReturnMappedDTOs_whenInputsAreValid_andStatusNotAvailable() throws PageOutOfRangeException {
        Book book = new Book("9783161484105", "Effective Java", false);
        book.setId(1L);

        Page<Book> bookPage = new PageImpl<>(List.of(book));
        SearchBookDTO search = new SearchBookDTO(0, 10, "Java", GenreType.TECHNOLOGY, "Joshua", "Bloch", null);

        when(bookDAO.findAllOrderedByTitle(eq("Java"), eq(GenreType.TECHNOLOGY), eq("Joshua"), eq("Bloch"), eq(null), any(Pageable.class)))
                .thenReturn(bookPage);

        Page<BookDTO> result = bookService.getAll(search);

        assertEquals(1, result.getTotalElements());
        BookDTO dto = result.getContent().getFirst();
        assertEquals("Effective Java", dto.title());
        assertEquals("9783161484105", dto.isbn());
        assertFalse(dto.status());
    }

    @Test
    void getAll_shouldThrowException_whenPageIsNegative() {
        SearchBookDTO search = new SearchBookDTO(-1, 10, null, null, null, null, null);
        PageOutOfRangeException ex = assertThrows(PageOutOfRangeException.class, () -> bookService.getAll(search));
        assertEquals("Page number cannot be negative", ex.getMessage());
    }

    @Test
    void getAll_shouldThrowException_whenSizeIsZero() {
        SearchBookDTO search = new SearchBookDTO(0, 0, null, null, null, null, null);
        PageOutOfRangeException ex = assertThrows(PageOutOfRangeException.class, () -> bookService.getAll(search));
        assertEquals("Page size must be greater than 0", ex.getMessage());
    }

    @Test
    void getAll_shouldThrowException_whenSizeTooLarge() {
        SearchBookDTO search = new SearchBookDTO(0, 999, null, null, null, null, null);
        PageOutOfRangeException ex = assertThrows(PageOutOfRangeException.class, () -> bookService.getAll(search));
        assertEquals("Page size cannot exceed 100", ex.getMessage());
    }

    @Test
    void getAll_shouldReturnEmptyPage_whenNoBooksFound() throws PageOutOfRangeException {
        SearchBookDTO search = new SearchBookDTO(0, 10, null, null, null, null, null);

        when(bookDAO.findAllOrderedByTitle(any(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(Page.empty());

        Page<BookDTO> result = bookService.getAll(search);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAll_shouldMapAuthorDTOProperly_whenAuthorsArePresent() throws Exception {
        Book book = new Book("9783161484105", "Effective Java", true);
        book.setId(1L);

        Author author = new Author("Joshua", null, "Bloch");
        book.setAuthors(List.of(author));

        Genre genre = new Genre(GenreType.TECHNOLOGY);
        book.setGenres(List.of(genre));

        Page<Book> page = new PageImpl<>(List.of(book));
        SearchBookDTO search = new SearchBookDTO(0, 10, null, null, null, null, null);

        when(bookDAO.findAllOrderedByTitle(any(), any(), any(), any(), any(), any(Pageable.class)))
                .thenReturn(page);

        Page<BookDTO> result = bookService.getAll(search);

        assertEquals(1, result.getContent().size());
        BookDTO dto = result.getContent().getFirst();

        assertEquals("Effective Java", dto.title());
        assertEquals("9783161484105", dto.isbn());
        assertTrue(dto.authors().stream().anyMatch(a ->
                "Joshua".equals(a.firstName()) && "Bloch".equals(a.lastName())));
    }

    @Test
    void getByIsbn_shouldReturnMappedDTO_whenBookExists() throws BookNotFoundException {
        // Arrange
        String isbn = "9783161484105";
        Book book = new Book(isbn, "Effective Java", true);
        book.setId(1L);

        when(bookDAO.findByISBN(isbn)).thenReturn(Optional.of(book));

        // Act
        Optional<BookDTO> result = bookService.getByIsbn(isbn);

        // Assert
        assertTrue(result.isPresent());
        BookDTO dto = result.get();

        assertEquals(1L, dto.id());
        assertEquals(isbn, dto.isbn());
        assertEquals("Effective Java", dto.title());
        assertTrue(dto.status());

        verify(bookDAO).findByISBN(isbn);
    }

    @Test
    void getByIsbn_shouldThrowException_whenBookNotFound() {
        // Arrange
        String isbn = "0000000000000";
        when(bookDAO.findByISBN(isbn)).thenReturn(Optional.empty());

        // Act & Assert
        BookNotFoundException ex = assertThrows(BookNotFoundException.class, () -> bookService.getByIsbn(isbn));

        assertEquals("Book with ISBN '0000000000000' was not found.", ex.getMessage());
        verify(bookDAO).findByISBN(isbn);
    }
}
