package com.ros.lms.domain.converters;

import com.ros.lms.domain.enums.Sex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SexConverterTest {

    private SexConverter converter;

    @BeforeEach
    void setUp() {
        converter = new SexConverter();
    }

    @Test
    void testConvertToDatabaseColumn_shouldReturnCorrectCode() {
        assertEquals('M', converter.convertToDatabaseColumn(Sex.MALE));
        assertEquals('F', converter.convertToDatabaseColumn(Sex.FEMALE));
    }

    @Test
    void testConvertToDatabaseColumn_withNull_shouldReturnNull() {
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void testConvertToEntityAttribute_shouldReturnCorrectEnum() {
        assertEquals(Sex.MALE, converter.convertToEntityAttribute('M'));
        assertEquals(Sex.FEMALE, converter.convertToEntityAttribute('F'));
    }

    @Test
    void testConvertToEntityAttribute_withNull_shouldReturnNull() {
        assertNull(converter.convertToEntityAttribute(null));
    }

    @Test
    void testConvertToEntityAttribute_withInvalidCode_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            converter.convertToEntityAttribute('X');
        });

        assertEquals("Unknown sex code: X", exception.getMessage());
    }
}
