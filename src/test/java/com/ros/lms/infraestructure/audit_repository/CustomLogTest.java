package com.ros.lms.infraestructure.audit_repository;

import com.ros.lms.infraestructure.aop.audit_repository.CustomLog;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomLogTest {

    @Test
    void builder_setsAllFieldsCorrectly() {
        CustomLog log = new CustomLog.Builder()
                .description("Issued a book")
                .staffUsername("staff1")
                .actionType("ISSUE")
                .memberUsername("member1")
                .bookIsbn("1234567890123")
                .loanId(42L)
                .build();

        assertEquals("Issued a book", log.description());
        assertEquals("staff1", log.staffUsername());
        assertEquals("ISSUE", log.actionType());
        assertEquals("member1", log.memberUsername());
        assertEquals("1234567890123", log.bookIsbn());
        assertEquals(42L, log.loanId());
    }

    @Test
    void builder_allowsPartialBuilds_missingFieldsAreNull() {
        CustomLog log = new CustomLog.Builder()
                .description("Partial log")
                .build();

        assertEquals("Partial log", log.description());
        assertNull(log.staffUsername());
        assertNull(log.actionType());
        assertNull(log.memberUsername());
        assertNull(log.bookIsbn());
        assertNull(log.loanId());
    }

    @Test
    void record_isImmutable() {
        CustomLog log = new CustomLog.Builder()
                .description("Immutable log")
                .build();

        // There's no way to change fields after build
        assertEquals("Immutable log", log.description());
    }

    @Test
    void equalsAndHashCode_shouldWorkForIdenticalValues() {
        CustomLog log1 = new CustomLog.Builder()
                .description("Same log")
                .staffUsername("staffX")
                .actionType("DELETE")
                .build();

        CustomLog log2 = new CustomLog.Builder()
                .description("Same log")
                .staffUsername("staffX")
                .actionType("DELETE")
                .build();

        assertEquals(log1, log2);
        assertEquals(log1.hashCode(), log2.hashCode());
    }

    @Test
    void toString_containsFieldValues() {
        CustomLog log = new CustomLog.Builder()
                .description("Check toString")
                .staffUsername("staffZ")
                .build();

        String logString = log.toString();
        assertTrue(logString.contains("Check toString"));
        assertTrue(logString.contains("staffZ"));
    }
}
