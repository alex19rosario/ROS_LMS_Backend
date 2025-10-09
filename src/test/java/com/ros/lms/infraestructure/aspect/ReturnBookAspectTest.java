package com.ros.lms.infraestructure.aspect;


import com.ros.lms.domain.dtos.ReturnBookDTO;
import com.ros.lms.domain.entities.*;
import com.ros.lms.domain.enums.LoanStatuses;
import com.ros.lms.infraestructure.aop.aspect.ReturnBookAspect;
import com.ros.lms.infraestructure.aop.audit_service.contracts.LoanAuditService;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReturnBookAspectTest {

    @Mock
    private LoanAuditService loanAuditService;

    @Mock
    private JoinPoint joinPoint;

    @InjectMocks
    private ReturnBookAspect returnBookAspect;

    @Test
    void afterReturningReturnBookAdvice_ShouldCallReturnBookAfterReturning_WithReturnedStatus() {
        // Arrange
        User staffUser = new User();
        staffUser.setUsername("staffUser");

        Staff staff = new Staff();
        staff.setUser(staffUser);

        User memberUser = new User();
        memberUser.setUsername("memberUser");

        Member member = new Member();
        member.setUser(memberUser);

        Book book = new Book();
        book.setIsbn("9783161484105");

        LoanStatus status = new LoanStatus(LoanStatuses.RETURNED);

        Loan returnedLoan = new Loan(member, book, status, staff);
        returnedLoan.setId(10L);

        // Act
        returnBookAspect.afterReturningReturnBookAdvice(returnedLoan);

        // Assert
        verify(loanAuditService, times(1)).returnBookAfterReturning(
                "staffUser",
                "memberUser",
                "9783161484105",
                true,  // Because status is RETURNED
                10L
        );
    }

    @Test
    void afterReturningReturnBookAdvice_ShouldCallReturnBookAfterReturning_WithReturnedLateStatus() {
        // Arrange
        User staffUser = new User();
        staffUser.setUsername("staffUser");

        Staff staff = new Staff();
        staff.setUser(staffUser);

        User memberUser = new User();
        memberUser.setUsername("memberUser");

        Member member = new Member();
        member.setUser(memberUser);

        Book book = new Book();
        book.setIsbn("9783161484105");

        LoanStatus status = new LoanStatus(LoanStatuses.RETURNED_LATE);

        Loan returnedLoan = new Loan(member, book, status, staff);
        returnedLoan.setId(20L);

        // Act
        returnBookAspect.afterReturningReturnBookAdvice(returnedLoan);

        // Assert
        verify(loanAuditService, times(1)).returnBookAfterReturning(
                "staffUser",
                "memberUser",
                "9783161484105",
                false,  // Because status is RETURNED_LATE
                20L
        );
    }

    @Test
    void afterThrowingReturnBookAdvice_ShouldCallReturnBookAfterThrowing() {
        // Arrange
        ReturnBookDTO dto = new ReturnBookDTO("9783161484105", "staffUser");
        Throwable ex = new RuntimeException("Something went wrong");

        when(joinPoint.getArgs()).thenReturn(new Object[]{dto});

        // Act
        returnBookAspect.afterThrowingReturnBookAdvice(joinPoint, ex);

        // Assert
        verify(loanAuditService, times(1)).returnBookAfterThrowing(
                "An error occurred while returning a book: Something went wrong",
                "staffUser",
                "9783161484105"
        );
    }
}
