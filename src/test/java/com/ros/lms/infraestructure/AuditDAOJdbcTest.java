package com.ros.lms.infraestructure;

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
import static org.mockito.Mockito.times;
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
        CustomLog log = new CustomLog("Test description", "Test action");

        // Act
        auditDAOJdbcImpl.createLog(log);

        // Assert
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> paramCaptor = ArgumentCaptor.forClass(Object.class);

        verify(jdbcTemplate, times(1)).update(
                queryCaptor.capture(),
                paramCaptor.capture(),
                paramCaptor.capture()
        );

        assertEquals("{call GENERATE_LOG(?, ?)}", queryCaptor.getValue());
        assertEquals("Test description", paramCaptor.getAllValues().get(0));
        assertEquals("Test action", paramCaptor.getAllValues().get(1));
    }
}
