package com.ros.lms.infraestructure.aop.audit_repository;

import jakarta.annotation.PostConstruct;
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
        PutItemEnhancedRequest<CustomLog> request = PutItemEnhancedRequest.builder(CustomLog.class)
                .item(log)
                .build();

        auditLogTable.putItem(request);
    }
}
