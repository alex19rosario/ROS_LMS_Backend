package com.ros.aop.audit_service;

import com.ros.aop.audit_repository.AuditDAO;
import com.ros.aop.audit_repository.CustomLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MemberAuditServiceImpl implements MemberAuditService {

    private final AuditDAO auditDAO;

    @Autowired
    public MemberAuditServiceImpl(@Qualifier("auditDAOJdbcImpl") AuditDAO auditDAO){
        this.auditDAO = auditDAO;
    }

    @Override
    public void logAddMemberAfterReturning(String description) {
        CustomLog log = new CustomLog(description, "NEW MEMBER WAS ADDED");
        auditDAO.createLog(log);
    }

    @Override
    public void logAddMemberAfterThrowing(String description) {
        CustomLog log = new CustomLog(description, "ERROR");
        auditDAO.createLog(log);
    }
}
