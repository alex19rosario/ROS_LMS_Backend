package com.ros.lms.infraestructure.audit_service;

import com.ros.lms.domain.enums.ActionType;
import com.ros.lms.infraestructure.aop.audit_repository.AuditDAO;
import com.ros.lms.infraestructure.aop.audit_repository.CustomLog;
import com.ros.lms.infraestructure.aop.audit_service.BookAuditServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class BookAuditServiceTest {

    @Mock
    AuditDAO auditDAO;

    @InjectMocks
    BookAuditServiceImpl bookAuditService;

    private CustomLog log;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogAddBookAfterReturning() {
        // Arrange
        String staffUsername = "adminUser";
        String bookIsbn = "9781234567890";

        // Act
        bookAuditService.logAddBookAfterReturning(staffUsername, bookIsbn);

        // Assert
        ArgumentCaptor<CustomLog> logCaptor = ArgumentCaptor.forClass(CustomLog.class);
        verify(auditDAO, times(1)).createLog(logCaptor.capture());

        CustomLog capturedLog = logCaptor.getValue();
        assertEquals("adminUser", capturedLog.staffUsername());
        assertEquals("9781234567890", capturedLog.bookIsbn());
        assertEquals(ActionType.NEW_BOOK_WAS_ADDED.getValue(), capturedLog.actionType()); // Make sure this matches your enum's getValue()
    }

    @Test
    void testLogAddBookAfterThrowing() {
        // Arrange
        String description = "Failed to add Book B due to DB error";
        String staffUsername = "adminUser";
        String bookIsbn = "9780987654321";

        // Act
        bookAuditService.logAddBookAfterThrowing(description, staffUsername, bookIsbn);

        // Assert
        ArgumentCaptor<CustomLog> logCaptor = ArgumentCaptor.forClass(CustomLog.class);
        verify(auditDAO, times(1)).createLog(logCaptor.capture());

        CustomLog capturedLog = logCaptor.getValue();
        assertEquals(description, capturedLog.description());
        assertEquals("adminUser", capturedLog.staffUsername());
        assertEquals("9780987654321", capturedLog.bookIsbn());
        assertEquals("ERROR", capturedLog.actionType());
    }
}
