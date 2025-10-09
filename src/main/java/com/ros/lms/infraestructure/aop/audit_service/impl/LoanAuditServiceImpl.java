package com.ros.lms.infraestructure.aop.audit_service.impl;

import com.ros.lms.domain.enums.ActionType;
import com.ros.lms.infraestructure.aop.audit_repository.AuditDao;
import com.ros.lms.infraestructure.aop.audit_repository.CustomLog;
import com.ros.lms.infraestructure.aop.audit_service.contracts.LoanAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class LoanAuditServiceImpl implements LoanAuditService {

    private final AuditDao auditDAO;

    @Autowired
    public LoanAuditServiceImpl(@Qualifier("auditDaoDynamoDbImpl") AuditDao auditDAO){
        this.auditDAO = auditDAO;
    }

    @Override
    public void addLoanAfterReturning(String staffUsername, String memberUsername, String bookIsbn, Long loanId) {

        CustomLog log = new CustomLog.Builder()
                .actionType(ActionType.A_BOOK_WAS_BORROWED.getValue())
                .staffUsername(staffUsername)
                .memberUsername(memberUsername)
                .bookIsbn(bookIsbn)
                .loanId(loanId)
                .build();

        auditDAO.createLog(log);
    }

    @Override
    public void addLoanAfterThrowing(String description, String staffUsername, String memberUsername, String bookIsbn) {

        CustomLog log = new CustomLog.Builder()
                .description(description)
                .actionType(ActionType.ERROR.getValue())
                .staffUsername(staffUsername)
                .memberUsername(memberUsername)
                .bookIsbn(bookIsbn)
                .build();

        auditDAO.createLog(log);
    }

    @Override
    public void returnBookAfterReturning(String staffUsername, String memberUsername, String bookIsbn, boolean isReturnedOnTime, Long loanId) {

        CustomLog log = new CustomLog.Builder()
                .actionType(isReturnedOnTime? ActionType.A_BOOK_WAS_RETURNED.getValue() : ActionType.A_BOOK_WAS_RETURNED_LATE.getValue())
                .staffUsername(staffUsername)
                .memberUsername(memberUsername)
                .bookIsbn(bookIsbn)
                .loanId(loanId)
                .build();

        auditDAO.createLog(log);
    }

    @Override
    public void returnBookAfterThrowing(String description, String staffUsername, String bookIsbn) {

        CustomLog log = new CustomLog.Builder()
                .description(description)
                .actionType(ActionType.ERROR.getValue())
                .staffUsername(staffUsername)
                .bookIsbn(bookIsbn)
                .build();

        auditDAO.createLog(log);
    }
}
