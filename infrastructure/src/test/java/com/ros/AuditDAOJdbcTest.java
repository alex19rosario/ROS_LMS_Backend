package com.ros;

import com.ros.aop.audit_repository.AuditDAOJdbcImpl;
import com.ros.aop.audit_repository.CustomLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AuditDAOJdbcTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private AuditDAOJdbcImpl auditDAOJdbcImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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
