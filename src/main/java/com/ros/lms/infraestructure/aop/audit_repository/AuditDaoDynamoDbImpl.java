package com.ros.lms.infraestructure.aop.audit_repository;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;

@Repository
public class AuditDaoDynamoDbImpl implements AuditDao {

    private static final String TABLE_NAME = "Logs";
    private final DynamoDbEnhancedClient dynamoDbEnhancedClient;
    private DynamoDbTable<CustomLog> auditLogTable;
    private static final Logger logger = LoggerFactory.getLogger(AuditDaoDynamoDbImpl.class);

    @Autowired
    public AuditDaoDynamoDbImpl(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.dynamoDbEnhancedClient = dynamoDbEnhancedClient;
    }

    @PostConstruct
    public void init() {
        auditLogTable = dynamoDbEnhancedClient.table(TABLE_NAME, TableSchema.fromBean(CustomLog.class));
    }

    @Override
    public void createLog(CustomLog log) {
        try {
            PutItemEnhancedRequest<CustomLog> request = PutItemEnhancedRequest.builder(CustomLog.class)
                    .item(log)
                    .build();

            auditLogTable.putItem(request);
        } catch (Exception e) {
            // Log the error without breaking the main flow
            logger.error("Failed to write log to DynamoDB: {}", e.getMessage(), e);
        }
    }
}
