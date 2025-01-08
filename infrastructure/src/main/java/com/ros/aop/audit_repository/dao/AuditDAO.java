package com.ros.aop.audit_repository.dao;

import com.ros.aop.audit_entity.CustomLog;

public interface AuditDAO {
    void createLog(CustomLog log);
}
