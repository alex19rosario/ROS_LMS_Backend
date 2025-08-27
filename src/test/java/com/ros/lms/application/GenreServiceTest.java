package com.ros.lms.application;

import com.ros.lms.domain.enums.GenreType;
import com.ros.lms.ports.outbound.repository_contracts.GenreDAO;
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
        Set<GenreType> mockEnumGenres = Set.of(
                GenreType.FICTION,
                GenreType.NON_FICTION,
                GenreType.SCIENCE
        );

        when(genreDAO.findAll()).thenReturn(mockEnumGenres);

        // Act
        Set<String> result = genreService.getAll();

        // Assert
        Set<String> expectedLabels = Set.of(
                GenreType.FICTION.getVal(),
                GenreType.NON_FICTION.getVal(),
                GenreType.SCIENCE.getVal()
        );

        assertEquals(expectedLabels, result, "The genres returned should match the expected labels");
        verify(genreDAO, times(1)).findAll();
    }
}
