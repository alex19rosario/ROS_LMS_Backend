package com.ros;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ros.dtos.AddBookDTO;
import com.ros.dtos.AuthorDTO;
import com.ros.exceptions.BookAlreadyExistsException;
import com.ros.ports_inbound.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private AddBookDTO validBookDTO;

    @BeforeEach
    void setup() {
        AuthorDTO author = new AuthorDTO("Joshua", "", "Bloch");
        validBookDTO = new AddBookDTO(
                9783161484105L,
                "Effective Java",
                Set.of(author),
                Set.of("SCIENCE")
        );
    }

    @Test
    @WithMockUser(username = "admin", roles={"STAFF"})
    void saveBook_shouldReturnOk_whenBookIsSaved() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBookDTO)))
                .andExpect(status().isOk());

        verify(bookService).save(validBookDTO);
    }

    @Test
    @WithMockUser(username = "admin", roles={"STAFF"})
    void saveBook_shouldReturnConflict_whenBookAlreadyExists() throws Exception {
        Mockito.doThrow(new BookAlreadyExistsException("Book already exists in the database"))
                .when(bookService).save(Mockito.any(AddBookDTO.class));

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBookDTO)))
                .andExpect(status().isConflict());

        verify(bookService).save(validBookDTO);
    }

    @Test
    @WithMockUser(username = "member", roles={"MEMBER"})
    void saveBook_shouldReturnUnauthorized_whenBookIsSaved() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBookDTO)))
                .andExpect(status().isForbidden());
    }
}

