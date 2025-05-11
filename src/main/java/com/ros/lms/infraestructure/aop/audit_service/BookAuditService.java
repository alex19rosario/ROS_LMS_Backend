package com.ros.lms.infraestructure.aop.audit_service;

public interface BookAuditService {
    void logAddBookAfterReturning(String description);
    void logAddBookAfterThrowing(String description);
}
