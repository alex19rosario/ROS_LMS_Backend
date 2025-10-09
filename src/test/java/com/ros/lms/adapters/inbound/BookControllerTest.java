package com.ros.lms.adapters.inbound;

import com.ros.lms.domain.dtos.AddBookDTO;
import com.ros.lms.domain.dtos.AuthorDTO;
import com.ros.lms.domain.dtos.BookDTO;
import com.ros.lms.domain.dtos.SearchBookDTO;
import com.ros.lms.domain.enums.GenreType;
import com.ros.lms.domain.exceptions.BookAlreadyExistsException;
import com.ros.lms.domain.exceptions.PageOutOfRangeException;
import com.ros.lms.domain.exceptions.StaffNotFoundException;
import com.ros.lms.domain.exceptions.StorageException;
import com.ros.lms.infraestructure.aop.audit_service.contracts.BookAuditService;
import com.ros.lms.ports.inbound.service_contracts.BookService;
import com.ros.lms.ports.inbound.service_contracts.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private BookAuditService bookAuditService;

    @MockitoBean
    private StorageService storageService;

    private AddBookDTO validBookDTO;

    private MockMultipartFile coverImage;

    @BeforeEach
    void setup() {
        coverImage = new MockMultipartFile(
                "coverImage",
                "cover.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "dummy-image-data".getBytes()
        );
        validBookDTO = new AddBookDTO(
                "9783161484105",
                "Effective Java",
                "Joshua-Bloch",
                "staff0001",
                "SCIENCE,TECHNOLOGY",
                coverImage
        );
    }

    @Test
    @WithMockUser(username = "staff", roles = {"STAFF"})
    void saveBook_shouldReturnOk_whenBookIsSaved() throws Exception {
        mockMvc.perform(
                multipart("/api/books")
                        .file(coverImage)
                        .param("isbn", String.valueOf(validBookDTO.isbn()))
                        .param("title", validBookDTO.title())
                        .param("authors", validBookDTO.authors())
                        .param("staffUsername", validBookDTO.staffUsername())
                        .param("genres", validBookDTO.genres())
        ).andExpect(status().isOk());

        verify(bookService).add(any(AddBookDTO.class));
    }

    @Test
    @WithMockUser(username = "staff", roles = {"STAFF"})
    void saveBook_shouldReturnConflict_whenBookAlreadyExists() throws Exception {
        doThrow(new BookAlreadyExistsException("Book already exists"))
                .when(bookService).add(any(AddBookDTO.class));

        mockMvc.perform(
                multipart("/api/books")
                        .file(coverImage)
                        .param("isbn", String.valueOf(validBookDTO.isbn()))
                        .param("title", validBookDTO.title())
                        .param("authors", validBookDTO.authors())
                        .param("staffUsername", validBookDTO.staffUsername())
                        .param("genres", validBookDTO.genres())
        ).andExpect(status().isConflict());

        verify(bookService).add(any(AddBookDTO.class));
    }


    @Test
    @WithMockUser(username = "member", roles = {"MEMBER"})
    void saveBook_shouldReturnUnauthorized_whenBookIsSaved() throws Exception {
        mockMvc.perform(
                multipart("/api/books")
                        .file(coverImage)
                        .param("ISBN", String.valueOf(validBookDTO.isbn()))
                        .param("title", validBookDTO.title())
                        .param("authors", validBookDTO.authors())
                        .param("staffUsername", validBookDTO.staffUsername())
                        .param("genres", validBookDTO.genres())
        ).andExpect(status().isForbidden()); // assuming access is restricted by Spring Security
    }

    @Test
    @WithMockUser(username = "staff", roles = {"STAFF"})
    void saveBook_shouldReturnBadRequest_whenFailToStoreFile() throws Exception {
        doThrow(new StorageException("Failed to store cover image"))
                .when(bookService).add(any(AddBookDTO.class));

        mockMvc.perform(
                multipart("/api/books")
                        .file(coverImage)
                        .param("isbn", String.valueOf(validBookDTO.isbn()))
                        .param("title", validBookDTO.title())
                        .param("authors", validBookDTO.authors())
                        .param("staffUsername", validBookDTO.staffUsername())
                        .param("genres", validBookDTO.genres())
        ).andExpect(status().isBadRequest());

        verify(bookService).add(any(AddBookDTO.class));
    }

    @Test
    @WithMockUser(username = "staff", roles = {"STAFF"})
    void saveBook_shouldReturnBadRequest_whenAuthorFormatIsInvalid() throws Exception {
        // Arrange
        String invalidAuthors = "Joshua Bloch"; // Missing hyphen (should be "Joshua-Bloch")

        // Mock the service to throw IllegalArgumentException when parsing authors
        doThrow(new IllegalArgumentException("Invalid author format: " + invalidAuthors))
                .when(bookService).add(any(AddBookDTO.class));

        // Act & Assert
        mockMvc.perform(
                multipart("/api/books")
                        .file(coverImage)
                        .param("isbn", String.valueOf(validBookDTO.isbn()))
                        .param("title", validBookDTO.title())
                        .param("authors", invalidAuthors) // Invalid format
                        .param("staffUsername", validBookDTO.staffUsername())
                        .param("genres", validBookDTO.genres())
        ).andExpectAll(
                status().isBadRequest(),
                result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    assertTrue(responseBody.contains("Invalid author format"));
                }
        );

        verify(bookService).add(any(AddBookDTO.class));
    }

    @Test
    @WithMockUser(username = "staff", roles = {"STAFF"})
    void saveBook_ShouldReturnBadRequest_WhenStorageExceptionIsThrown() throws Exception {
        // Arrange: simulate a StorageException thrown from BookService
        doThrow(new StorageException("Invalid file storage path"))
                .when(bookService)
                .add(any());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/books")
                        .file(coverImage)
                        .param("ISBN", "1234567890")
                        .param("title", "Some Book")
                        .param("authors", "InvalidFormat")
                        .param("staffUsername", "staff")
                        .param("genres", "Fiction"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "staff", roles = {"STAFF"})
    void saveBook_shouldReturnNotFound_whenStaffIsMissing() throws Exception {
        // Arrange
        doThrow(new StaffNotFoundException("Staff with username 'missingStaff' not found"))
                .when(bookService).add(any(AddBookDTO.class));

        // Act & Assert
        mockMvc.perform(multipart("/api/books")
                                .file(coverImage)
                                .param("isbn", String.valueOf(validBookDTO.isbn()))
                                .param("title", validBookDTO.title())
                                .param("authors", validBookDTO.authors())
                                .param("staffUsername", validBookDTO.staffUsername()) // should be the one that causes the exception
                                .param("genres", validBookDTO.genres()))
                .andExpect(status().isNotFound());

        verify(bookService).add(any(AddBookDTO.class));
    }


    @Test
    @WithMockUser(username = "member", roles = {"MEMBER"})
    void getAllBooks_shouldReturnOkWithResults() throws Exception {
        List<BookDTO> books = List.of(
                new BookDTO(1L, "1234567890123", "Sample Book", Set.of(), Set.of(), true, "/path/image.jpg")
        );
        PageImpl<BookDTO> bookPage = new PageImpl<>(books, PageRequest.of(0, 10), 1);

        when(bookService.getAll( new SearchBookDTO(0, 10, null, null, null, null, null))).thenReturn(bookPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains("Sample Book")));
    }

    @Test
    @WithMockUser(username = "member", roles = {"MEMBER"})
    void getAllBooks_shouldApplyFiltersCorrectly() throws Exception {
        SearchBookDTO expectedDTO = new SearchBookDTO(0, 10, "Java", GenreType.TECHNOLOGY, "Joshua", "Bloch", null);
        when(bookService.getAll(expectedDTO))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 10), 0));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books")
                        .param("title", "Java")
                        .param("genre", "TECHNOLOGY")
                        .param("authorFirstName", "Joshua")
                        .param("authorLastName", "Bloch"))
                .andExpect(status().isOk());

        verify(bookService).getAll(expectedDTO);
    }

    @Test
    @WithMockUser(username = "member", roles = {"MEMBER"})
    void getAllBooks_shouldReturnBadRequest_whenPageSizeIsTooLarge() throws Exception {
        when(bookService.getAll(any(SearchBookDTO.class)))
                .thenThrow(new PageOutOfRangeException("Page size too large"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books")
                        .param("size", "999"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "member", roles = {"MEMBER"})
    void getAllBooks_shouldReturnBadRequest_whenPageIsNegative() throws Exception {
        when(bookService.getAll(any(SearchBookDTO.class)))
                .thenThrow(new PageOutOfRangeException("Negative page"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books")
                        .param("page", "-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "member", roles = {"MEMBER"})
    void getAllBooks_shouldReturnEmptyPage() throws Exception {
        PageImpl<BookDTO> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(bookService.getAll(new SearchBookDTO(0, 10, null, null, null, null, null)))
                .thenReturn(emptyPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(result -> assertTrue(result.getResponse().getContentAsString().contains("\"totalElements\":0")));
    }

    @Test
    @WithMockUser(username = "member", roles = {"MEMBER"})
    void getBookByIsbn_shouldReturnBook_whenBookExists() throws Exception {
        // Given
        String isbn = "9783161484105";
        BookDTO existingBookDTO = new BookDTO(
                1L,
                isbn,
                "Effective Java",
                Set.of(new AuthorDTO("Joshua", "Bloch")),
                Set.of(GenreType.SCIENCE, GenreType.TECHNOLOGY),
                true,
                "/images/cover.jpg"
        );

        when(bookService.getByIsbn(isbn)).thenReturn(Optional.of(existingBookDTO));

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/{isbn}", isbn))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isbn").value(isbn))
                .andExpect(jsonPath("$.title").value("Effective Java"))
                .andExpect(jsonPath("$.authors[0].firstName").value("Joshua"))
                .andExpect(jsonPath("$.authors[0].lastName").value("Bloch"))
                .andExpect(jsonPath("$.genres").isArray())
                .andExpect(jsonPath("$.genres").value(org.hamcrest.Matchers.containsInAnyOrder("SCIENCE", "TECHNOLOGY")))
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.imagePath").value("/images/cover.jpg"));

        verify(bookService).getByIsbn(isbn);
    }

    @Test
    @WithMockUser(username = "member", roles = {"MEMBER"})
    void getBookByIsbn_shouldReturnNotFound_whenBookDoesNotExist() throws Exception {
        // Given
        String isbn = "0000000000000";
        when(bookService.getByIsbn(isbn)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/{isbn}", isbn))
                .andExpect(status().isNotFound());

        verify(bookService).getByIsbn(isbn);
    }


}

