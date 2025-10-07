package com.ros.lms.infraestructure.aop.audit_service;


import com.ros.lms.domain.enums.ActionType;
import com.ros.lms.infraestructure.aop.audit_repository.AuditDao;
import com.ros.lms.infraestructure.aop.audit_repository.CustomLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookAuditServiceImpl implements BookAuditService {

    private final AuditDao auditDAO;

    @Autowired
    public BookAuditServiceImpl(@Qualifier("auditDaoDynamoDbImpl") AuditDao auditDAO){
        this.auditDAO = auditDAO;
    }

    @Transactional
    @Override
    public void logAddBookAfterReturning(String staffUsername, String bookIsbn) {

        CustomLog log = new CustomLog.Builder()
                .staffUsername(staffUsername)
                .actionType(ActionType.NEW_BOOK_WAS_ADDED.getValue())
                .bookIsbn(bookIsbn)
                .build();

        auditDAO.createLog(log);
    }

    @Transactional
    @Override
    public void logAddBookAfterThrowing(String description, String staffUsername, String bookIsbn) {

        CustomLog log = new CustomLog.Builder()
                .description(description)
                .staffUsername(staffUsername)
                .actionType(ActionType.ERROR.getValue())
                .bookIsbn(bookIsbn)
                .build();
        
        auditDAO.createLog(log);
    }
}
