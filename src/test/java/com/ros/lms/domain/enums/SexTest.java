package com.ros.lms.domain.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SexTest {
    @Test
    void testGetCode_shouldReturnCorrectChar() {
        assertEquals('M', Sex.MALE.getCode());
        assertEquals('F', Sex.FEMALE.getCode());
    }

    @Test
    void testFromCode_shouldReturnCorrectEnum() {
        assertEquals(Sex.MALE, Sex.fromCode('M'));
        assertEquals(Sex.FEMALE, Sex.fromCode('F'));
    }

    @Test
    void testFromCode_withInvalidCode_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Sex.fromCode('X');
        });
        assertEquals("Unknown sex code: X", exception.getMessage());
    }
}
