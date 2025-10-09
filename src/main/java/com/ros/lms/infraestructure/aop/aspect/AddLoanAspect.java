package com.ros.lms.infraestructure.aop.aspect;

import com.ros.lms.domain.dtos.AddLoanDTO;
import com.ros.lms.domain.entities.Loan;
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
public class AddLoanAspect {

    private final LoanAuditService loanAuditService;

    @Autowired
    public AddLoanAspect(@Qualifier("loanAuditServiceImpl") LoanAuditService loanAuditService) {
        this.loanAuditService = loanAuditService;
    }

    @Pointcut("execution(public com.ros.lms.domain.entities.Loan com.ros.lms.ports.inbound.service_contracts.LoanService.add(..))")
    private void forAddLoanMethod(){}

    @AfterReturning(pointcut = "forAddLoanMethod()", returning = "returnedLoan")
    public void afterReturningAddLoanAdvice(Loan returnedLoan) {
        loanAuditService.addLoanAfterReturning(
                returnedLoan.getStaff().getUser().getUsername(),
                returnedLoan.getMember().getUser().getUsername(),
                returnedLoan.getBook().getIsbn(),
                returnedLoan.getId()
        );
    }

    @AfterThrowing(pointcut = "forAddLoanMethod()", throwing = "ex")
    public void afterThrowingAddLoanAdvice(JoinPoint joinPoint, Throwable ex) {
        AddLoanDTO dto = (AddLoanDTO) joinPoint.getArgs()[0];
        String description = "An error occurred while adding loan: " + ex.getMessage();
        loanAuditService.addLoanAfterThrowing(description, dto.staffUsername(), dto.memberUsername(), dto.bookIsbn());
    }
}
