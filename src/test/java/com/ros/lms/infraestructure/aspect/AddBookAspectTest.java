package com.ros.lms.infraestructure.aspect;

import com.ros.lms.infraestructure.aop.aspect.AddBookAspect;
import com.ros.lms.infraestructure.aop.audit_service.BookAuditService;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddBookAspectTest {

    @Mock
    private BookAuditService bookAuditService;

    @Mock
    private JoinPoint joinPoint;

    @InjectMocks
    private AddBookAspect addBookAspect;

    private MockMultipartFile coverImage;

    @Test
    void afterReturningAddBookAdvice_ShouldCallLogAddBookAfterReturning() {
        // Arrange
        String isbn = "9783161484105";
        String staffUsername = "staff";
        Object[] args = new Object[]{
                isbn, // index 0
                "Effective Java",
                "Joshua-Bloch",
                "SCIENCE,TECHNOLOGY",
                staffUsername, // index 4
                coverImage
        };

        when(joinPoint.getArgs()).thenReturn(args);

        // Act
        addBookAspect.afterReturningAddBookAdvice(joinPoint);

        // Assert
        verify(bookAuditService, times(1))
                .logAddBookAfterReturning(staffUsername, isbn);
    }

    @Test
    void afterThrowingAddBookAdvice_ShouldCallLogAddBookAfterThrowing() {
        // Arrange
        String isbn = "9783161484105";
        String staffUsername = "staff";
        Object[] args = new Object[]{
                isbn,
                "Effective Java",
                "Joshua-Bloch",
                "SCIENCE,TECHNOLOGY",
                staffUsername,
                coverImage
        };

        Throwable ex = new RuntimeException("Something went wrong");

        when(joinPoint.getArgs()).thenReturn(args);

        // Act
        addBookAspect.afterThrowingAddBookAdvice(joinPoint, ex);

        // Assert
        verify(bookAuditService, times(1))
                .logAddBookAfterThrowing(
                        "An error occurred while adding book: Something went wrong",
                        isbn,           // <-- Correct order
                        staffUsername   // <-- Correct order
                );
    }

}
