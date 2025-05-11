package com.ros.lms.infraestructure.aop.audit_repository;

public interface AuditDAO {
    void createLog(CustomLog log);
}
