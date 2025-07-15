package com.ros.lms.infraestructure.aspect;

import com.ros.lms.domain.dtos.AddMemberDTO;
import com.ros.lms.domain.enums.Sex;
import com.ros.lms.infraestructure.aop.aspect.AddMemberAspect;
import com.ros.lms.infraestructure.aop.audit_service.MemberAuditService;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddMemberAspectTest {

    @Mock
    private MemberAuditService memberAuditService;

    @Mock
    private JoinPoint joinPoint;

    @InjectMocks
    private AddMemberAspect addMemberAspect;

    private AddMemberDTO addMemberDTO;

    @BeforeEach
    void setUp() {
        addMemberDTO = new AddMemberDTO(
                "321321321",
                "John",
                "Doe",
                "6578932134",
                LocalDate.of(1985, 7, 25),
                Sex.MALE,
                "john@example.com",
                "john",
                "test123",
                "staffUser"
        );
    }

    @Test
    void afterReturningAddMemberAdvice_ShouldCallLogAddMemberAfterReturning() {
        // Arrange
        when(joinPoint.getArgs()).thenReturn(new Object[]{addMemberDTO});

        // Act
        addMemberAspect.afterReturningAddMemberAdvice(joinPoint);

        // Assert
        verify(memberAuditService, times(1)).logAddMemberAfterReturning(addMemberDTO);
    }

    @Test
    void afterThrowingAddMemberAdvice_ShouldCallLogAddMemberAfterThrowing() {
        // Arrange
        when(joinPoint.getArgs()).thenReturn(new Object[]{addMemberDTO});
        Throwable ex = new RuntimeException("Database connection failure");

        // Act
        addMemberAspect.afterThrowingAddMemberAdvice(joinPoint, ex);

        // Assert
        verify(memberAuditService, times(1))
                .logAddMemberAfterThrowing(addMemberDTO, "An error occurred while adding member: Database connection failure");
    }


}
