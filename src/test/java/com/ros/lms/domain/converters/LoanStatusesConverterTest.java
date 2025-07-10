package com.ros.lms.domain.converters;

import com.ros.lms.domain.enums.LoanStatuses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoanStatusesConverterTest {
    private LoanStatusesConverter converter;

    @BeforeEach
    void setUp() {
        converter = new LoanStatusesConverter();
    }

    @Test
    void testConvertToDatabaseColumn_shouldReturnCorrectString() {
        assertEquals("LOANED", converter.convertToDatabaseColumn(LoanStatuses.LOANED));
        assertEquals("RETURNED-LATE", converter.convertToDatabaseColumn(LoanStatuses.RETURNED_LATE));
    }

    @Test
    void testConvertToDatabaseColumn_withNull_shouldReturnNull() {
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void testConvertToEntityAttribute_shouldReturnCorrectEnum() {
        assertEquals(LoanStatuses.RETURNED, converter.convertToEntityAttribute("RETURNED"));
        assertEquals(LoanStatuses.LOST, converter.convertToEntityAttribute("lost")); // Case-insensitive
    }

    @Test
    void testConvertToEntityAttribute_withNull_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
                converter.convertToEntityAttribute(null));
    }

    @Test
    void testConvertToEntityAttribute_withInvalidValue_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                converter.convertToEntityAttribute("INVALID"));

        assertEquals("Unknown loan status value: INVALID", exception.getMessage());
    }
}
