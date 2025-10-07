package com.ros.lms.infraestructure.audit_service;

import com.ros.lms.domain.dtos.AddMemberDTO;
import com.ros.lms.domain.enums.Sex;
import com.ros.lms.infraestructure.aop.audit_repository.AuditDao;
import com.ros.lms.infraestructure.aop.audit_repository.CustomLog;
import com.ros.lms.infraestructure.aop.audit_service.MemberAuditServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class MemberAuditServiceTest{

    @Mock
    AuditDao auditDAO;

    @InjectMocks
    MemberAuditServiceImpl memberAuditService;

    private CustomLog log;

    private AddMemberDTO testMemberDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testMemberDTO = new AddMemberDTO(
                "123456789",
                "John",
                "Doe",
                "5551234567",
                LocalDate.of(1990, 1, 1),
                Sex.MALE,
                "john.doe@example.com",
                "johndoe",
                "password123",
                "adminUser"
        );
    }


    @Test
    void testLogAddMemberAfterReturning() {
        // Act
        memberAuditService.logAddMemberAfterReturning(testMemberDTO);

        // Assert
        ArgumentCaptor<CustomLog> logCaptor = ArgumentCaptor.forClass(CustomLog.class);
        verify(auditDAO, times(1)).createLog(logCaptor.capture());

        CustomLog capturedLog = logCaptor.getValue();
        assertEquals("adminUser", capturedLog.getStaffUsername());
        assertEquals("johndoe", capturedLog.getMemberUsername());
        assertEquals("NEW MEMBER WAS ADDED", capturedLog.getActionType());
    }

    @Test
    void testLogAddMemberAfterThrowing() {
        // Arrange
        String description = "Failed to add member due to duplicate username";

        // Act
        memberAuditService.logAddMemberAfterThrowing(testMemberDTO, description);

        // Assert
        ArgumentCaptor<CustomLog> logCaptor = ArgumentCaptor.forClass(CustomLog.class);
        verify(auditDAO, times(1)).createLog(logCaptor.capture());

        CustomLog capturedLog = logCaptor.getValue();
        assertEquals(description, capturedLog.getDescription());
        assertEquals("adminUser", capturedLog.getStaffUsername());
        assertEquals("johndoe", capturedLog.getMemberUsername());
        assertEquals("ERROR", capturedLog.getActionType());
    }
}
