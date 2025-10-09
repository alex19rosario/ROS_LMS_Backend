package com.ros.lms.infraestructure.aop.aspect;

import com.ros.lms.domain.dtos.ReturnBookDTO;
import com.ros.lms.domain.entities.Loan;
import com.ros.lms.domain.enums.LoanStatuses;
import com.ros.lms.infraestructure.aop.audit_service.contracts.LoanAuditService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ReturnBookAspect {

    private final LoanAuditService loanAuditService;

    @Autowired
    public ReturnBookAspect(@Qualifier("loanAuditServiceImpl") LoanAuditService loanAuditService) {
        this.loanAuditService = loanAuditService;
    }

    @Pointcut("execution(public com.ros.lms.domain.entities.Loan com.ros.lms.ports.inbound.service_contracts.LoanService.returnBook(..))")
    private void forReturnBookMethod(){}

    @AfterReturning(pointcut = "forReturnBookMethod()", returning = "returnedLoan")
    public void afterReturningReturnBookAdvice(Loan returnedLoan) {
        loanAuditService.returnBookAfterReturning(
                returnedLoan.getStaff().getUser().getUsername(),
                returnedLoan.getMember().getUser().getUsername(),
                returnedLoan.getBook().getIsbn(),
                returnedLoan.getStatus().getCode().equals(LoanStatuses.RETURNED),
                returnedLoan.getId()
        );
    }

    @AfterThrowing(pointcut = "forReturnBookMethod()", throwing = "ex")
    public void afterThrowingReturnBookAdvice(JoinPoint joinPoint, Throwable ex) {
        ReturnBookDTO dto = (ReturnBookDTO) joinPoint.getArgs()[0];
        String description = "An error occurred while returning a book: " + ex.getMessage();
        loanAuditService.returnBookAfterThrowing(description, dto.staffUsername(), dto.isbn());
    }


}
