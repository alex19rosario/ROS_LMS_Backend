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

    @Test
    void setters_shouldUpdateFieldsCorrectly() {
        // Arrange
        CustomLog log = new CustomLog();

        // Act
        log.setLogId("log123");
        log.setTimeStamp("2025-10-09T12:00:00Z");
        log.setDescription("Book returned");
        log.setStaffUsername("staffUser");
        log.setActionType("RETURN");
        log.setMemberUsername("memberUser");
        log.setBookIsbn("9783161484105");
        log.setLoanId(99L);

        // Assert
        assertEquals("log123", log.getLogId());
        assertEquals("2025-10-09T12:00:00Z", log.getTimeStamp());
        assertEquals("Book returned", log.getDescription());
        assertEquals("staffUser", log.getStaffUsername());
        assertEquals("RETURN", log.getActionType());
        assertEquals("memberUser", log.getMemberUsername());
        assertEquals("9783161484105", log.getBookIsbn());
        assertEquals(99L, log.getLoanId());
    }

    @Test
    void setters_canOverrideBuilderValues() {
        // Arrange
        CustomLog log = new CustomLog.Builder()
                .description("Initial description")
                .staffUsername("staffA")
                .actionType("ISSUE")
                .memberUsername("memberA")
                .bookIsbn("123")
                .loanId(1L)
                .build();

        // Act (override all)
        log.setDescription("Updated description");
        log.setStaffUsername("staffB");
        log.setActionType("RETURN");
        log.setMemberUsername("memberB");
        log.setBookIsbn("456");
        log.setLoanId(2L);

        // Assert (confirm updates)
        assertEquals("Updated description", log.getDescription());
        assertEquals("staffB", log.getStaffUsername());
        assertEquals("RETURN", log.getActionType());
        assertEquals("memberB", log.getMemberUsername());
        assertEquals("456", log.getBookIsbn());
        assertEquals(2L, log.getLoanId());
    }

    @Test
    void setters_shouldAllowNullAssignments() {
        // Arrange
        CustomLog log = new CustomLog();

        // Act
        log.setDescription(null);
        log.setStaffUsername(null);
        log.setActionType(null);
        log.setMemberUsername(null);
        log.setBookIsbn(null);
        log.setLoanId(null);

        // Assert
        assertNull(log.getDescription());
        assertNull(log.getStaffUsername());
        assertNull(log.getActionType());
        assertNull(log.getMemberUsername());
        assertNull(log.getBookIsbn());
        assertNull(log.getLoanId());
    }

    @Test
    void equals_shouldReturnFalseForDifferentValues() {
        CustomLog log1 = new CustomLog.Builder().description("A").build();
        CustomLog log2 = new CustomLog.Builder().description("B").build();

        assertNotEquals(log1, log2);
    }


}
