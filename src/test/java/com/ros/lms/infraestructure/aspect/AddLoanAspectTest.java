package com.ros.lms.infraestructure.aspect;

import com.ros.lms.domain.dtos.AddLoanDTO;
import com.ros.lms.domain.entities.*;
import com.ros.lms.domain.enums.LoanStatuses;
import com.ros.lms.infraestructure.aop.aspect.AddLoanAspect;
import com.ros.lms.infraestructure.aop.audit_service.contracts.LoanAuditService;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddLoanAspectTest {

    @Mock
    private LoanAuditService loanAuditService;

    @Mock
    private JoinPoint joinPoint;

    @InjectMocks
    private AddLoanAspect addLoanAspect;

    @Test
    void afterReturningAddLoanAdvice_ShouldCallAddLoanAfterReturning() {
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

        LoanStatus status = new LoanStatus(LoanStatuses.LOANED);

        Loan loan = new Loan(member, book, status, staff);
        loan.setId(1L);

        // Act
        addLoanAspect.afterReturningAddLoanAdvice(loan);

        // Assert
        verify(loanAuditService, times(1)).addLoanAfterReturning(
                "staffUser",
                "memberUser",
                "9783161484105",
                1L
        );
    }

    @Test
    void afterThrowingAddLoanAdvice_ShouldCallAddLoanAfterThrowing() {
        // Arrange
        AddLoanDTO dto = new AddLoanDTO(
                "9783161484105",
                "memberUser",
                "staffUser"
        );

        Throwable ex = new RuntimeException("Something went wrong");

        when(joinPoint.getArgs()).thenReturn(new Object[]{dto});

        // Act
        addLoanAspect.afterThrowingAddLoanAdvice(joinPoint, ex);

        // Assert
        verify(loanAuditService, times(1)).addLoanAfterThrowing(
                "An error occurred while adding loan: Something went wrong",
                "staffUser",
                "memberUser",
                "9783161484105"
        );
    }

}
