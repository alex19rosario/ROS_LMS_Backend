package com.ros.lms.infraestructure.aspect;

import com.ros.lms.domain.dtos.AddBookDTO;
import com.ros.lms.infraestructure.aop.aspect.AddBookAspect;
import com.ros.lms.infraestructure.aop.audit_service.BookAuditService;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
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

    private AddBookDTO addBookDTO;

    private MockMultipartFile coverImage;

    @BeforeEach
    void setUp(){
        coverImage = new MockMultipartFile(
                "coverImage",
                "cover.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "dummy-image-data".getBytes()
        );
        addBookDTO = new AddBookDTO(
                "9783161484105",
                "Effective Java",
                "Joshua-Bloch",
                "SCIENCE,TECHNOLOGY",
                "staff",
                coverImage
        );
    }

    @Test
    void afterReturningAddBookAdvice_ShouldCallLogAddBookAfterReturning() {
        // Arrange
        Object[] args = new Object[]{addBookDTO};
        when(joinPoint.getArgs()).thenReturn(args);

        // Act
        addBookAspect.afterReturningAddBookAdvice(joinPoint);

        // Assert
        verify(bookAuditService, times(1)).logAddBookAfterReturning(Long.valueOf(addBookDTO.isbn()).toString());
    }

    @Test
    void afterThrowingAddBookAdvice_ShouldCallLogAddBookAfterThrowing() {
        // Act
        addBookAspect.afterThrowingAddBookAdvice();

        // Assert
        verify(bookAuditService, times(1))
                .logAddBookAfterThrowing("An error occurred while adding a book");
    }
}
