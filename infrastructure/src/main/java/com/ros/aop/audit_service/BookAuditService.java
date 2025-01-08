package com.ros.aop.audit_service;

public interface BookAuditService {
    void logAddBookAfterReturning(String description);
    void logAddBookAfterThrowing(String description);
}
