package com.ros.aop.audit_service.service;

import com.ros.aop.audit_entity.CustomLog;

public interface BookAuditService {
    void logAddBookAfter(CustomLog log);
}
