package com.ros.aop.audit_repository;

public interface AuditDAO {
    void createLog(CustomLog log);
}
