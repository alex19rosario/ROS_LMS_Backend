package com.ros.lms.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionTypeTest {

    @Test
    @DisplayName("fromValue should return correct enum constant (case-insensitive)")
    void fromValue_ValidInput_ReturnsEnum() {
        assertEquals(ActionType.NEW_BOOK_WAS_ADDED, ActionType.fromValue("new book was added"));
        assertEquals(ActionType.A_BOOK_WAS_BORROWED, ActionType.fromValue("A BOOK WAS BORROWED"));
        assertEquals(ActionType.A_BOOK_WAS_RETURNED, ActionType.fromValue("a book was returned"));
        assertEquals(ActionType.OVERDUE, ActionType.fromValue("OVERDUE"));
        assertEquals(ActionType.NEW_MEMBER_WAS_ADDED, ActionType.fromValue("new member was added"));
        assertEquals(ActionType.NEW_STAFF_WAS_ADDED, ActionType.fromValue("NEW STAFF WAS ADDED"));
        assertEquals(ActionType.ERROR, ActionType.fromValue("error"));
    }

    @Test
    @DisplayName("fromValue should throw IllegalArgumentException for invalid input")
    void fromValue_InvalidInput_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ActionType.fromValue("INVALID ACTION");
        });

        assertTrue(exception.getMessage().contains("Unknown ActionType"));
    }

    @Test
    @DisplayName("getValue should return the correct string representation")
    void getValue_ReturnsCorrectValue() {
        assertEquals("NEW BOOK WAS ADDED", ActionType.NEW_BOOK_WAS_ADDED.getValue());
        assertEquals("A BOOK WAS BORROWED", ActionType.A_BOOK_WAS_BORROWED.getValue());
        assertEquals("A BOOK WAS RETURNED", ActionType.A_BOOK_WAS_RETURNED.getValue());
        assertEquals("OVERDUE", ActionType.OVERDUE.getValue());
        assertEquals("NEW MEMBER WAS ADDED", ActionType.NEW_MEMBER_WAS_ADDED.getValue());
        assertEquals("NEW STAFF WAS ADDED", ActionType.NEW_STAFF_WAS_ADDED.getValue());
        assertEquals("ERROR", ActionType.ERROR.getValue());
    }

    @Test
    @DisplayName("toString should return same as getValue()")
    void toString_EqualsGetValue() {
        for (ActionType type : ActionType.values()) {
            assertEquals(type.getValue(), type.toString());
        }
    }
}
