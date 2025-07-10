package com.ros.lms.domain.converters;

import com.ros.lms.domain.enums.GenreType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreTypeConverterTest {

    private GenreTypeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new GenreTypeConverter();
    }

    @Test
    void testConvertToDatabaseColumn_shouldReturnCorrectLabel() {
        assertEquals("SCIENCE FICTION", converter.convertToDatabaseColumn(GenreType.SCIENCE_FICTION));
        assertEquals("POETRY", converter.convertToDatabaseColumn(GenreType.POETRY));
    }

    @Test
    void testConvertToDatabaseColumn_withNull_shouldReturnNull() {
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void testConvertToEntityAttribute_shouldReturnCorrectEnum() {
        assertEquals(GenreType.SCIENCE_FICTION, converter.convertToEntityAttribute("SCIENCE FICTION"));
        assertEquals(GenreType.POETRY, converter.convertToEntityAttribute("POETRY"));
    }

    @Test
    void testConvertToEntityAttribute_withNull_shouldReturnNull() {
        assertNull(converter.convertToEntityAttribute(null));
    }

    @Test
    void testConvertToEntityAttribute_withInvalidLabel_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                converter.convertToEntityAttribute("UNKNOWN GENRE"));

        assertEquals("Unknown genre label: UNKNOWN GENRE", exception.getMessage());
    }
}
