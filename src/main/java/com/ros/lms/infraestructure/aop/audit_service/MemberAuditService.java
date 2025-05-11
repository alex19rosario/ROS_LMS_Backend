package com.ros.lms.infraestructure.aop.audit_service;

public interface MemberAuditService {
    void logAddMemberAfterReturning(String description);
    void logAddMemberAfterThrowing(String description);
}
