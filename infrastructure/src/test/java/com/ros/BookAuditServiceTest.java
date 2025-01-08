package com.ros;

import com.ros.aop.audit_repository.AuditDAO;
import com.ros.aop.audit_repository.CustomLog;
import com.ros.aop.audit_service.BookAuditService;
import com.ros.aop.audit_service.BookAuditServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BookAuditServiceTest {

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
        String description = "Book A was added";

        // Act
        bookAuditService.logAddBookAfterReturning(description);

        // Assert
        ArgumentCaptor<CustomLog> logCaptor = ArgumentCaptor.forClass(CustomLog.class);
        verify(auditDAO, times(1)).createLog(logCaptor.capture());

        CustomLog capturedLog = logCaptor.getValue();
        assertEquals("Book A was added", capturedLog.description());
        assertEquals("NEW BOOK WAS ADDED", capturedLog.actionType());
    }

    @Test
    void testLogAddBookAfterThrowing() {
        // Arrange
        String description = "Failed to add Book B";

        // Act
        bookAuditService.logAddBookAfterThrowing(description);

        // Assert
        ArgumentCaptor<CustomLog> logCaptor = ArgumentCaptor.forClass(CustomLog.class);
        verify(auditDAO, times(1)).createLog(logCaptor.capture());

        CustomLog capturedLog = logCaptor.getValue();
        assertEquals("Failed to add Book B", capturedLog.description());
        assertEquals("ERROR", capturedLog.actionType());
    }
}
