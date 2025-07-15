package com.ros.lms.infraestructure.aop.audit_service;

public interface BookAuditService {
    void logAddBookAfterReturning(String staffUsername, String bookIsbn);
    void logAddBookAfterThrowing(String description, String staffUsername, String bookIsbn);
}
