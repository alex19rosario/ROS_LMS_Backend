package com.ros.lms.domain.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GenreTypeTest {

    @Test
    void testGetLabel_shouldReturnCorrectLabel() {
        assertEquals("MYSTERY", GenreType.MYSTERY.getLabel());
        assertEquals("SCIENCE FICTION", GenreType.SCIENCE_FICTION.getLabel());
        assertEquals("CHILDREN LITERATURE", GenreType.CHILDREN_LITERATURE.getLabel());
    }

    @Test
    void testFromLabel_shouldReturnCorrectEnumIgnoringCase() {
        assertEquals(GenreType.MYSTERY, GenreType.fromLabel("mystery"));
        assertEquals(GenreType.SCIENCE_FICTION, GenreType.fromLabel("SCIENCE FICTION"));
        assertEquals(GenreType.POETRY, GenreType.fromLabel("Poetry"));
    }

    @Test
    void testFromLabel_withInvalidLabel_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            GenreType.fromLabel("Unknown Genre");
        });
        assertEquals("Unknown genre label: Unknown Genre", exception.getMessage());
    }
}
