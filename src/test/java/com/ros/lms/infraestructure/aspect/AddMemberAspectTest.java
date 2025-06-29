package com.ros.lms.infraestructure.aspect;

import com.ros.lms.domain.dtos.AddMemberDTO;
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
    void setUp(){

        addMemberDTO = new AddMemberDTO(
                "321321321",
                "John",
                "Doe",
                "6578932134",
                LocalDate.of(1985, 7, 25),
                'M' ,
                "john@example.com",
                "john",
                "test123");
    }

    @Test
    void afterReturningAddMemberAdvice_ShouldCallLogAddMemberAfterReturning() {
        // Arrange
        Object[] args = new Object[]{addMemberDTO};
        when(joinPoint.getArgs()).thenReturn(args);

        // Act
        addMemberAspect.afterReturningAddMemberAdvice(joinPoint);

        // Assert
        verify(memberAuditService, times(1)).logAddMemberAfterReturning(addMemberDTO.governmentID());
    }

    @Test
    void afterThrowingAddMemberAdvice_ShouldCallLogAddMemberAfterThrowing() {
        // Act
        addMemberAspect.afterThrowingAddMemberAdvice();

        // Assert
        verify(memberAuditService, times(1))
                .logAddMemberAfterThrowing("An error occurred while adding a member");
    }


}
