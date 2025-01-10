package com.ros;

import com.ros.ports_inbound.service.GenreService;
import com.ros.ports_inbound.serviceImpl.GenreServiceImpl;
import com.ros.ports_outbound.dao.GenreDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GenreServiceTest {

    @Mock
    private GenreDAO genreDAO;

    @InjectMocks
    private GenreServiceImpl genreService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllGenres() {
        // Arrange
        Set<String> mockGenres = Set.of("Fiction", "Non-Fiction", "Science");
        when(genreDAO.findAll()).thenReturn(mockGenres);

        // Act
        Set<String> result = genreService.getAll();

        // Assert
        assertEquals(mockGenres, result, "The genres returned should match the mock data");
        verify(genreDAO, times(1)).findAll();
    }
}
