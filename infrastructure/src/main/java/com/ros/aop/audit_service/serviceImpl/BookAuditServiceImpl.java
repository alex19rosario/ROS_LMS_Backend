package com.ros.aop.audit_service.serviceImpl;

import com.ros.aop.audit_entity.CustomLog;
import com.ros.aop.audit_repository.dao.AuditDAO;
import com.ros.aop.audit_service.service.BookAuditService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BookAuditServiceImpl implements BookAuditService {

    private final AuditDAO auditDAO;

    public BookAuditServiceImpl(@Qualifier("auditDAOJdbcImpl") AuditDAO auditDAO){
        this.auditDAO = auditDAO;
    }

    @Override
    public void logAddBookAfter(CustomLog log) {
        auditDAO.createLog(log);
    }
}
