package com.ros.lms.adapters.inbound;


import com.ros.lms.ports.inbound.service_contracts.GenreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GenreService genreService;

    @Test
    @WithMockUser(username = "staff", roles={"STAFF"})
    public void testGetAllGenresAsAdmin() throws Exception {
        // Arrange
        when(genreService.getAll()).thenReturn(Set.of("Fiction", "Non-Fiction", "Science"));

        // Act & Assert
        mockMvc.perform(get("/api/genres")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @WithMockUser(username = "member", roles={"MEMBER"})
    public void testGetAllGenresAsUnauthorizedUser() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/genres")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
