package com.ros.aop.audit_service;

import com.ros.aop.audit_repository.AuditDAO;
import com.ros.aop.audit_repository.CustomLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookAuditServiceImpl implements BookAuditService {

    private final AuditDAO auditDAO;

    @Autowired
    public BookAuditServiceImpl(@Qualifier("auditDAOJdbcImpl") AuditDAO auditDAO){
        this.auditDAO = auditDAO;
    }

    @Transactional
    @Override
    public void logAddBookAfterReturning(String description) {
        CustomLog log = new CustomLog(description, "NEW BOOK WAS ADDED");
        auditDAO.createLog(log);
    }

    @Transactional
    @Override
    public void logAddBookAfterThrowing(String description) {
        CustomLog log = new CustomLog(description, "ERROR");
        auditDAO.createLog(log);
    }
}
