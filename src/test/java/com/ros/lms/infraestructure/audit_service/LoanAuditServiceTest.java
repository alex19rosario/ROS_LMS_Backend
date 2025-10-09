package com.ros.lms.infraestructure.audit_service;

import com.ros.lms.domain.enums.ActionType;
import com.ros.lms.infraestructure.aop.audit_repository.AuditDao;
import com.ros.lms.infraestructure.aop.audit_repository.CustomLog;
import com.ros.lms.infraestructure.aop.audit_service.impl.LoanAuditServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class LoanAuditServiceTest {
    @Mock
    private AuditDao auditDAO;

    @InjectMocks
    private LoanAuditServiceImpl loanAuditService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addLoanAfterReturning_ShouldCreateCorrectLog() {
        // Arrange
        String staffUsername = "staffUser";
        String memberUsername = "memberUser";
        String bookIsbn = "9783161484105";
        Long loanId = 123L;

        // Act
        loanAuditService.addLoanAfterReturning(staffUsername, memberUsername, bookIsbn, loanId);

        // Assert
        ArgumentCaptor<CustomLog> logCaptor = ArgumentCaptor.forClass(CustomLog.class);
        verify(auditDAO, times(1)).createLog(logCaptor.capture());
        CustomLog log = logCaptor.getValue();

        assertEquals(ActionType.A_BOOK_WAS_BORROWED.getValue(), log.getActionType());
        assertEquals(staffUsername, log.getStaffUsername());
        assertEquals(memberUsername, log.getMemberUsername());
        assertEquals(bookIsbn, log.getBookIsbn());
        assertEquals(loanId, log.getLoanId());
    }

    @Test
    void addLoanAfterThrowing_ShouldCreateErrorLog() {
        // Arrange
        String description = "An error occurred while adding loan: Something went wrong";
        String staffUsername = "staffUser";
        String memberUsername = "memberUser";
        String bookIsbn = "9783161484105";

        // Act
        loanAuditService.addLoanAfterThrowing(description, staffUsername, memberUsername, bookIsbn);

        // Assert
        ArgumentCaptor<CustomLog> logCaptor = ArgumentCaptor.forClass(CustomLog.class);
        verify(auditDAO, times(1)).createLog(logCaptor.capture());
        CustomLog log = logCaptor.getValue();

        assertEquals(ActionType.ERROR.getValue(), log.getActionType());
        assertEquals(description, log.getDescription());
        assertEquals(staffUsername, log.getStaffUsername());
        assertEquals(memberUsername, log.getMemberUsername());
        assertEquals(bookIsbn, log.getBookIsbn());
    }

    @Test
    void returnBookAfterReturning_ShouldCreateReturnedOnTimeLog() {
        // Arrange
        String staffUsername = "staffUser";
        String memberUsername = "memberUser";
        String bookIsbn = "9783161484105";
        Long loanId = 555L;
        boolean isReturnedOnTime = true;

        // Act
        loanAuditService.returnBookAfterReturning(staffUsername, memberUsername, bookIsbn, isReturnedOnTime, loanId);

        // Assert
        ArgumentCaptor<CustomLog> logCaptor = ArgumentCaptor.forClass(CustomLog.class);
        verify(auditDAO, times(1)).createLog(logCaptor.capture());
        CustomLog log = logCaptor.getValue();

        assertEquals(ActionType.A_BOOK_WAS_RETURNED.getValue(), log.getActionType());
        assertEquals(staffUsername, log.getStaffUsername());
        assertEquals(memberUsername, log.getMemberUsername());
        assertEquals(bookIsbn, log.getBookIsbn());
        assertEquals(loanId, log.getLoanId());
    }

    @Test
    void returnBookAfterReturning_ShouldCreateReturnedLateLog() {
        // Arrange
        String staffUsername = "staffUser";
        String memberUsername = "memberUser";
        String bookIsbn = "9783161484105";
        Long loanId = 777L;
        boolean isReturnedOnTime = false;

        // Act
        loanAuditService.returnBookAfterReturning(staffUsername, memberUsername, bookIsbn, isReturnedOnTime, loanId);

        // Assert
        ArgumentCaptor<CustomLog> logCaptor = ArgumentCaptor.forClass(CustomLog.class);
        verify(auditDAO, times(1)).createLog(logCaptor.capture());
        CustomLog log = logCaptor.getValue();

        assertEquals(ActionType.A_BOOK_WAS_RETURNED_LATE.getValue(), log.getActionType());
        assertEquals(staffUsername, log.getStaffUsername());
        assertEquals(memberUsername, log.getMemberUsername());
        assertEquals(bookIsbn, log.getBookIsbn());
        assertEquals(loanId, log.getLoanId());
    }

    @Test
    void returnBookAfterThrowing_ShouldCreateErrorLog() {
        // Arrange
        String description = "An error occurred while returning a book: Database timeout";
        String staffUsername = "staffUser";
        String bookIsbn = "9783161484105";

        // Act
        loanAuditService.returnBookAfterThrowing(description, staffUsername, bookIsbn);

        // Assert
        ArgumentCaptor<CustomLog> logCaptor = ArgumentCaptor.forClass(CustomLog.class);
        verify(auditDAO, times(1)).createLog(logCaptor.capture());
        CustomLog log = logCaptor.getValue();

        assertEquals(ActionType.ERROR.getValue(), log.getActionType());
        assertEquals(description, log.getDescription());
        assertEquals(staffUsername, log.getStaffUsername());
        assertEquals(bookIsbn, log.getBookIsbn());
    }
}
