package com.ros.aop.audit_service;

public interface BookAuditService {
    void logAddBookAfterReturning();
    void logAddBookAfterThrowing();
}
