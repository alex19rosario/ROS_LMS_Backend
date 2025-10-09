package com.ros.lms.infraestructure.aop.audit_service.contracts;

public interface LoanAuditService {
    void addLoanAfterReturning(String staffUsername,
                               String memberUsername,
                               String bookIsbn,
                               Long loanId);

    void addLoanAfterThrowing(String description,
                              String staffUsername,
                              String memberUsername,
                              String bookIsbn);

    void returnBookAfterReturning(String staffUsername,
                                  String memberUsername,
                                  String bookIsbn,
                                  boolean isReturnedOnTime,
                                  Long loanId);

    void returnBookAfterThrowing(String description,
                                 String staffUsername,
                                 String bookIsbn);

}
