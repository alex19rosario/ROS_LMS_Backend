package com.ros.lms.infraestructure.audit_repository;

import com.ros.lms.infraestructure.aop.audit_repository.AuditDAOJdbcImpl;
import com.ros.lms.infraestructure.aop.audit_repository.CustomLog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuditDAOJdbcTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private AuditDAOJdbcImpl auditDAOJdbcImpl;

    @Test
    void testCreateLog() {
        // Arrange
        CustomLog log = new CustomLog(
                "Test description",
                "staff123",
                "Test action",
                "member456",
                "1231231231",
                42L
        );

        // Act
        auditDAOJdbcImpl.createLog(log);

        // Assert
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object[]> paramCaptor = ArgumentCaptor.forClass(Object[].class);

        verify(jdbcTemplate).update(queryCaptor.capture(), paramCaptor.capture());

        assertEquals("{call GENERATE_LOG(?, ?, ?, ?, ?, ?)}", queryCaptor.getValue());

        Object[] params = paramCaptor.getValue();
        assertEquals(6, params.length);
        assertEquals("Test description", params[0]);
        assertEquals("Test action", params[1]);
        assertEquals("staff123", params[2]);
        assertEquals("member456", params[3]);
        assertEquals("1231231231", params[4]);
        assertEquals(42L, params[5]);
    }
}
