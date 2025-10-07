package com.ros.lms.infraestructure.aop.audit_repository;

public interface AuditDao {
    void createLog(CustomLog log);
}
