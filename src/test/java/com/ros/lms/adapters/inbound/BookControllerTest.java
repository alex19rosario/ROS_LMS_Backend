package com.ros.lms.adapters.inbound;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ros.lms.domain.dtos.AddBookDTO;
import com.ros.lms.domain.exceptions.BookAlreadyExistsException;
import com.ros.lms.domain.exceptions.StorageException;
import com.ros.lms.infraestructure.aop.audit_service.BookAuditService;
import com.ros.lms.ports.inbound.service_contracts.BookService;
import com.ros.lms.ports.inbound.service_contracts.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private BookAuditService bookAuditService;

    @MockitoBean
    private StorageService storageService;

    @Autowired
    private ObjectMapper objectMapper;

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
                9783161484105L,
                "Effective Java",
                "Joshua-Bloch",
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
                        .param("isbn", String.valueOf(validBookDTO.ISBN()))
                        .param("title", validBookDTO.title())
                        .param("authors", validBookDTO.authors())
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
                        .param("isbn", String.valueOf(validBookDTO.ISBN()))
                        .param("title", validBookDTO.title())
                        .param("authors", validBookDTO.authors())
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
                        .param("ISBN", String.valueOf(validBookDTO.ISBN()))
                        .param("title", validBookDTO.title())
                        .param("authors", validBookDTO.authors())
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
                        .param("isbn", String.valueOf(validBookDTO.ISBN()))
                        .param("title", validBookDTO.title())
                        .param("authors", validBookDTO.authors())
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
                        .param("isbn", String.valueOf(validBookDTO.ISBN()))
                        .param("title", validBookDTO.title())
                        .param("authors", invalidAuthors) // Invalid format
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
    void addBook_ShouldReturnBadRequest_WhenStorageExceptionIsThrown() throws Exception {
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
                        .param("genres", "Fiction"))
                .andExpect(status().isBadRequest());
    }



}

