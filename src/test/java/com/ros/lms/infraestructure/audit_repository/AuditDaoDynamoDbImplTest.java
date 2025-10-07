package com.ros.lms.infraestructure.audit_repository;


import com.ros.lms.infraestructure.aop.audit_repository.AuditDaoDynamoDbImpl;
import com.ros.lms.infraestructure.aop.audit_repository.CustomLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditDaoDynamoDbImplTest {

    @Mock
    private DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @Mock
    private DynamoDbTable<CustomLog> dynamoDbTable;

    @InjectMocks
    private AuditDaoDynamoDbImpl auditDaoDynamoDbImpl;

    @BeforeEach
    void setUp() {
        // Mock behavior for table mapping
        when(dynamoDbEnhancedClient.table(eq("Logs"), any(TableSchema.class)))
                .thenReturn(dynamoDbTable);

        // Manually trigger @PostConstruct logic
        auditDaoDynamoDbImpl.init();
    }

    @Test
    void testCreateLog() {
        // Arrange
        CustomLog log = new CustomLog.Builder()
                .description("Test description")
                .staffUsername("staff123")
                .actionType("Test action")
                .memberUsername("member456")
                .bookIsbn("1231231231")
                .loanId(42L)
                .build();

        ArgumentCaptor<PutItemEnhancedRequest<CustomLog>> requestCaptor =
                ArgumentCaptor.forClass(PutItemEnhancedRequest.class);

        // Act
        auditDaoDynamoDbImpl.createLog(log);

        // Assert
        verify(dynamoDbTable).putItem(requestCaptor.capture());
        PutItemEnhancedRequest<CustomLog> capturedRequest = requestCaptor.getValue();

        CustomLog capturedLog = capturedRequest.item();

        assertEquals("Test description", capturedLog.getDescription());
        assertEquals("Test action", capturedLog.getActionType());
        assertEquals("staff123", capturedLog.getStaffUsername());
        assertEquals("member456", capturedLog.getMemberUsername());
        assertEquals("1231231231", capturedLog.getBookIsbn());
        assertEquals(42L, capturedLog.getLoanId());
    }

}
