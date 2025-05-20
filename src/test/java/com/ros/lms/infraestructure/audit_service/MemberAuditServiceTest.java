package com.ros.lms.infraestructure.audit_service;

import com.ros.lms.infraestructure.aop.audit_repository.AuditDAO;
import com.ros.lms.infraestructure.aop.audit_repository.CustomLog;
import com.ros.lms.infraestructure.aop.audit_service.MemberAuditServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MemberAuditServiceTest{

    @Mock
    AuditDAO auditDAO;

    @InjectMocks
    MemberAuditServiceImpl memberAuditService;

    private CustomLog log;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogAddMemberAfterReturning() {
        // Arrange
        String description = "Member A was added";

        // Act
        memberAuditService.logAddMemberAfterReturning(description);

        // Assert
        ArgumentCaptor<CustomLog> logCaptor = ArgumentCaptor.forClass(CustomLog.class);
        verify(auditDAO, times(1)).createLog(logCaptor.capture());

        CustomLog capturedLog = logCaptor.getValue();
        assertEquals("Member A was added", capturedLog.description());
        assertEquals("NEW MEMBER WAS ADDED", capturedLog.actionType());
    }

    @Test
    void testLogAddMemberAfterThrowing() {
        // Arrange
        String description = "Failed to add Member B";

        // Act
        memberAuditService.logAddMemberAfterThrowing(description);

        // Assert
        ArgumentCaptor<CustomLog> logCaptor = ArgumentCaptor.forClass(CustomLog.class);
        verify(auditDAO, times(1)).createLog(logCaptor.capture());

        CustomLog capturedLog = logCaptor.getValue();
        assertEquals("Failed to add Member B", capturedLog.description());
        assertEquals("ERROR", capturedLog.actionType());
    }
}
