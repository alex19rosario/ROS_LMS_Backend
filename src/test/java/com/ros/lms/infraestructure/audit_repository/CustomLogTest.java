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

        assertNotNull(log.getLogId(), "logId should be auto-generated");
        assertNotNull(log.getTimeStamp(), "timeStamp should be auto-generated");

        assertEquals("Issued a book", log.getDescription());
        assertEquals("staff1", log.getStaffUsername());
        assertEquals("ISSUE", log.getActionType());
        assertEquals("member1", log.getMemberUsername());
        assertEquals("1234567890123", log.getBookIsbn());
        assertEquals(42L, log.getLoanId());
    }

    @Test
    void builder_allowsPartialBuilds_missingFieldsAreNull() {
        CustomLog log = new CustomLog.Builder()
                .description("Partial log")
                .build();

        assertNotNull(log.getLogId());
        assertNotNull(log.getTimeStamp());

        assertEquals("Partial log", log.getDescription());
        assertNull(log.getStaffUsername());
        assertNull(log.getActionType());
        assertNull(log.getMemberUsername());
        assertNull(log.getBookIsbn());
        assertNull(log.getLoanId());
    }

    @Test
    void builder_generatesUniqueIdsAndTimestamps() {
        CustomLog log1 = new CustomLog.Builder().description("A").build();
        CustomLog log2 = new CustomLog.Builder().description("B").build();

        assertNotEquals(log1.getLogId(), log2.getLogId(), "Each log should have a unique logId");
        assertNotEquals(log1.getTimeStamp(), log2.getTimeStamp(), "Each log should have a unique timestamp");
    }

    @Test
    void equalsAndHashCode_shouldWorkForIdenticalValues() {
        CustomLog log1 = new CustomLog.Builder()
                .description("Same log")
                .staffUsername("staffX")
                .actionType("DELETE")
                .memberUsername("member1")
                .bookIsbn("1111")
                .loanId(1L)
                .build();

        CustomLog log2 = new CustomLog.Builder()
                .description("Same log")
                .staffUsername("staffX")
                .actionType("DELETE")
                .memberUsername("member1")
                .bookIsbn("1111")
                .loanId(1L)
                .build();

        assertEquals(log1, log2);
        assertEquals(log1.hashCode(), log2.hashCode());
    }

    @Test
    void toString_containsFieldValues() {
        CustomLog log = new CustomLog.Builder()
                .description("Check toString")
                .staffUsername("staffZ")
                .actionType("UPDATE")
                .build();

        String logString = log.toString();

        assertTrue(logString.contains("Check toString"));
        assertTrue(logString.contains("staffZ"));
        assertTrue(logString.contains("UPDATE"));
        assertTrue(logString.contains("logId"));
        assertTrue(logString.contains("timeStamp"));
    }
}
